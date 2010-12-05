(ns robust-txt.core
  (:require
    [robust-txt.utils :as utils]
    [clojure.contrib.io :as io]
    [clojure.contrib.str-utils2 :as su]
    [clj-http.client :as client])
  (:import
    [clojure.lang Sequential]
    [java.io InputStream]
    [java.net URL])
  (:gen-class))

;(set! *warn-on-reflection* true)

(defn- trim-comment
  "Removes everything after the first # character in a String."
  [line]
  (su/replace line #"#.*$" ""))

(defn- process-user-agent
  "Set the current user-agent and add it to the list of user-agents."
  [directives user-agent value]
  (dosync
    (ref-set user-agent value)
    (alter directives assoc @user-agent [])))

(defn- process-allow
  "Set an allow directive for the current user-agent."
  [directives user-agent value]
  (dosync
    (let [permissions (@directives @user-agent)]
      (alter directives
             assoc @user-agent (vec (conj permissions [:allow value]))))))

(defn- process-disallow
  "Set a disallow directive for the current user-agent."
  [directives user-agent value]
  (dosync
    (let [permissions (@directives @user-agent)]
      (alter directives
             assoc @user-agent (vec (conj permissions [:disallow value]))))))

(defn- parse-key
  "Parse the key in a directive."
  [key]
  (keyword (su/lower-case (su/trim key))))

(defn- parse-value
  "Parse the value in a directive."
  [key value]
  (let [t (if (nil? value) "" (su/trim value))]
    (if (contains? #{:crawl-delay :request-rate} key)
      (utils/parse-int t)
      t)))

(defn- parse-line
  "Parse a line from a robots.txt file."
  [line]
  (let [[left right]  (su/split (trim-comment line) #":" 2)
        key           (parse-key left)
        value         (parse-value key right)]
    (if (= "" value) nil [key value])))

(defn- parse-lines
  "Parse the lines of the robots.txt file."
  [lines]
  (let [user-agent (ref "*")
        directives (ref {"*" []})]
    (doseq [line lines]
      (let [[key value] (parse-line line)]
        (cond
          (or (nil? key) (nil? value))
            nil
          (= key :user-agent) (process-user-agent directives user-agent value)
          (= key :allow)      (process-allow directives user-agent value)
          (= key :disallow)   (process-disallow directives user-agent value)
          :default            (dosync (alter directives assoc key value)))))
    (dosync
      (alter directives assoc :modified-time (System/currentTimeMillis)))
    @directives))

(defn get-robots
  "Download robots.txt for a particular URL."
  [url]
  (try
    (let [domain (.getHost ^URL (io/as-url url))
          response (client/get (str "http://" domain "/robots.txt"))]
      (response :body))
    (catch Exception e "")))

(defn crawlable?
  "Returns true if a list of directives allows the path to be crawled using
  this interpretation of robots.txt:

  http://www.robotstxt.org/

  Note that allow directives are completely ignored and only the first
  disallow directive is consulted to determine if a path can be crawled."
  [directives ^String path & {:keys [user-agent] :or {user-agent "*"}}]
  (let [select-disallows #(= :disallow (first %))
        permissions (filter select-disallows (get directives user-agent))]
    (and (nil? (some #(.startsWith path (last %)) permissions))
         (if (not= "*" user-agent)
           (crawlable? directives path :user-agent "*")
           true))))

(defmulti parse-robots class)

(defmethod parse-robots
  Sequential [lines]
  (parse-lines lines))

(defmethod parse-robots
  String [string]
  (parse-robots (su/split-lines string)))

(defmethod parse-robots
  InputStream [stream]
  (parse-robots (utils/stream-to-string stream)))

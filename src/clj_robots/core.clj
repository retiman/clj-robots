(ns clj-robots.core
  (:refer-clojure :exclude (get))
  (:use
    [clojure.contrib.def])
  (:require
    [clojure.string :as s]
    [clojure.contrib.io :as io]
    [clj-robots.utils :as utils]
    [clj-httpc.client :as client])
  (:import
    [clojure.lang Sequential]
    [java.io InputStream]
    [java.net URL])
  (:gen-class))

(defvar- mget clojure.core/get)

(defvar- directive-keys
  #{"user-agent"
    "allow"
    "disallow"
    "crawl-delay"
    "request-rate"
    "robot-version"
    "visit-time"
    "sitemap"})

(defn- trim-comment
  "Removes everything after the first # character in a String."
  [line]
  (s/replace line #"#.*$" ""))

(defn- process-user-agent
  "Set the current user-agent and add it to the list of user-agents."
  [directives user-agents last-key value]
  (dosync
    (alter directives assoc :sitemap [])
    (if (= @last-key :user-agent)
      (alter user-agents conj value)
      (ref-set user-agents #{value}))
    (doseq [ua @user-agents]
      (alter directives assoc ua []))))

(defn- process-permission
  "Set an allow or disallow directive for the current user-agent."
  [directives user-agents key value]
  (dosync
    (doseq [ua @user-agents]
      (let [permissions (@directives ua)]
        (alter directives assoc ua (vec (conj permissions [key value])))))))

(defn- process-sitemap
  "Add a sitemap."
  [directives value]
  (dosync
    (let [sitemap (mget @directives :sitemap)]
      (alter directives assoc :sitemap (vec (conj sitemap value))))))

(defn process-request-rate
  "Convert a Request-rate to a Ratio.  The ratio represents the number of
  documents that should be fetched per second (default).  If a time unit
  other than seconds is used, then it is converted to seconds."
  [s]
  (let [m (case (last s) \h 3600 \m 60 \s 1 1)
        t (first (s/split s #"[^0-9/]"))
        [p q] (if-not (nil? t) (map utils/parse-int (s/split t #"/" 2)))]
    (cond
      (nil? p) nil
      (nil? q) nil
      :default (/ p (* m q)))))

(defn- parse-key
  "Parse the key in a directive."
  [key]
  (let [k (s/lower-case (s/trim key))]
    (if (contains? directive-keys k)
      (keyword k))))

(defn- parse-value
  "Parse the value in a directive."
  [key value]
  (cond (nil? value)          ""
        (= key :crawl-delay)  ((comp utils/parse-int s/trim) value)
        (= key :request-rate) ((comp process-request-rate s/trim) value)
        :default              (s/trim value)))

(defn- parse-line
  "Parse a line from a robots.txt file."
  [line]
  (let [[left right]  (s/split (trim-comment line) #":" 2)
        key           (parse-key left)
        value         (parse-value key right)]
    (if (not= "" value) [key value])))

(defn- parse-lines
  "Parse the lines of the robots.txt file."
  [lines]
  (dosync
    (let [last-key (ref nil)
          user-agents (ref #{"*"})
          directives (ref {"*" []})]
      (doseq [line lines]
        (let [[key value] (parse-line line)]
          (cond
            (or (nil? key) (nil? value))
              nil
            (= key :user-agent)
              (process-user-agent directives user-agents last-key value)
            (= key :sitemap)
              (process-sitemap directives value)
            (contains? #{:allow :disallow} key)
              (process-permission directives user-agents key value)
            :default
              (alter directives assoc key value))
          (ref-set last-key key)))
      (alter directives assoc :modified-time (System/currentTimeMillis))
    @directives)))

(defmulti get-url
  "Returns the robots.txt URL for a particular host (given a URL)."
  class)

(defmethod get-url URL [url]
  (let [protocol (.getProtocol url)
        domain (.getHost url)]
    (str protocol "://" domain "/robots.txt")))

(defmethod get-url String [url]
  (get-url (io/as-url url)))

(defmulti get
  "Download robots.txt for a particular URL."
  class)

(defmethod get URL [url]
  (try
    (let [robots-url (get-url url)
          response (client/get robots-url)]
      (response :body))
    (catch Exception e "")))

(defmethod get String [url]
  (get (io/as-url url)))

(defn crawlable?
  "Returns true if a list of directives allows the path to be crawled using
  this interpretation of robots.txt:

  http://www.robotstxt.org/

  Note that allow directives are completely ignored and only the first
  disallow directive is consulted to determine if a path can be crawled."
  [directives ^String path & {:keys [user-agent] :or {user-agent "*"}}]
  (let [select-disallows #(= :disallow (first %))
        permissions (filter select-disallows (mget directives user-agent))]
    (and (nil? (some #(.startsWith path (last %)) permissions))
         (if (not= "*" user-agent)
           (crawlable? directives path :user-agent "*")
           true))))

(defmulti parse
  "Parse robots.txt; returns a data structure to pass to crawlable?"
  class)

(defmethod parse
  Sequential [lines]
  (parse-lines lines))

(defmethod parse
  String [string]
  (parse (s/split-lines string)))

(defmethod parse
  InputStream [stream]
  (parse (utils/stream-to-string stream)))

(defmethod parse
  nil [arg]
  nil)

(def
  ^{:doc "DEPRECATED: Prefer get."
    :deprecated "0.5.0"}
  get-robots get)

(def
  ^{:doc "DEPRECATED: Prefer get-url."
    :deprecated "0.5.0"}
  get-robots-url get-url)

(def
  ^{:doc "DEPRECATED: Prefer parse."
    :deprecated "0.5.0"}
  parse-robots parse)

(ns clj-robots.utils
  (:require
    [clojure.string :as s]
    [clojure.contrib.duck-streams :as ds]
    [clojure.contrib.io :as io])
  (:import
    [java.io StringWriter]
    [org.apache.commons.io IOUtils])
  (:gen-class))

(defn load-resource
  "Return a resource located on path."
  [path]
  (let [t (Thread/currentThread)
        loader (.getContextClassLoader t)]
    (.getResourceAsStream loader path)))

(defn stream-to-string
  "Convert an InputStream to a String."
  [stream]
  (let [writer (new StringWriter)]
    (do
      (IOUtils/copy stream writer)
      (.toString writer))))

(defn wildcard-to-regex
  "Convert a wildcard pattern to a Java Pattern for matching paths.
  Note that .* is added to the end of the pattern for this reason."
  [text]
  (let [sb (StringBuffer. (count text))]
    (doseq [c text]
      (cond
        (= c \*)
          (.append sb ".*")
        (= c \?)
          (.append sb ".")
        (contains? #{\( \) \[ \] \$ \^ \. \{ \} \| \\} c)
          (doto sb
            (.append \\)
            (.append c))
        :default
          (.append sb c)))
    (.append sb ".*")
    (re-pattern (.toString sb))))

(defn get-lines
  "Load a resource, convert it to a string, and return a vector of lines."
  [resource]
  ((comp s/split-lines stream-to-string load-resource) resource))

(defn parse-int
  "Convert a String to an Integer."
  [s]
  (try (Integer/parseInt s) (catch NumberFormatException e nil)))

(defn parse-ratio
  "Convert a Request-rate to a Ratio.  The ratio represents the number of
  documents that should be fetched per second (default).  If a time unit
  other than seconds is used, then it is converted to seconds."
  [s]
  (let [m (case (last s) \h 3600 \m 60 \s 1 1)
        t (first (s/split s #"[^0-9/]"))
        [p q] (if-not (nil? t) (map parse-int (s/split t #"/" 2)))]
    (cond
      (nil? p) nil
      (nil? q) nil
      :default (/ p (* m q)))))

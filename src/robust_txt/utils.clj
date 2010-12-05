(ns robust-txt.utils
  (:require
    [clojure.contrib.duck-streams :as ds]
    [clojure.contrib.io :as io]
    [clojure.contrib.str-utils2 :as su])
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

(defn get-lines
  "Load a resource, convert it to a string, and return a vector of lines."
  [resource]
  ((comp su/split-lines stream-to-string load-resource) resource))

(defn parse-int
  "Convert a String to an integer or return 0."
  [s]
  (try (Integer/parseInt s) (catch NumberFormatException e nil)))

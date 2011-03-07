(ns clj-robots.utils
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

(defn wildcard-to-regex
  "Convert a wildcard pattern to a Java Pattern."
  [text]
  (let [sb (StringBuffer. (count text))]
    (doseq [c text]
      (case c
        \*
          (.append sb ".*")
        \?
          (.append sb ".")
        \( \) \[ \] \$ \^ \. \{ \} \| \\
          (do (.append sb "\\") (.append sb))
        (.append sb c)))
    (re-pattern (.toString sb))))

(defn get-lines
  "Load a resource, convert it to a string, and return a vector of lines."
  [resource]
  ((comp su/split-lines stream-to-string load-resource) resource))

(defn parse-int
  "Convert a String to an Integer."
  [s]
  (try (Integer/parseInt s) (catch NumberFormatException e nil)))

(defn parse-ratio
  "Convert a String to a Ratio."
  [s]
  (let [t (map parse-int (su/split s #"/" 2))
        p (first t)
        q (second t)]
    (cond (nil? p) nil
          (nil? q) nil
          :default (/ p q))))

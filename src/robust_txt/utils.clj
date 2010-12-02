(ns robust-txt.utils
  (:require
    [clojure.contrib.duck-streams :as ds]
    [clojure.contrib.io :as io]
    [clojure.contrib.str-utils2 :as s])
  (:import
    [java.io StringWriter]
    [org.apache.commons.io IOUtils])
  (:gen-class))

(defn load-resource
  "Return a resource located on path."
  [path]
  (let [t (Thread/currentThread)
        loader (.getContextClassLoader t)]
    (. loader getResourceAsStream path)))

(defn stream-to-string
  "Convert an InputStream to a String."
  [stream]
  (let [writer (new StringWriter)]
    (do
      (IOUtils/copy stream writer)
      (. writer toString))))

(ns robust-txt.utils
  (:require
    [clojure.contrib.duck-streams :as ds]
    [clojure.contrib.io :as io]
    [clojure.contrib.str-utils2 :as s])
  (:import
    [java.io StringWriter]
    [org.apache.commons.io IOUtils]))

(defn load-resource [fqn]
  (let [t (Thread/currentThread)
        loader (.getContextClassLoader t)]
    (. loader getResourceAsStream fqn)))

(defn stream-to-string [stream]
  (let [writer (new StringWriter)]
    (do
      (IOUtils/copy stream writer)
      (. writer toString))))

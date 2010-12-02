(ns robust-txt.standard
  (:require
    [robust-txt.utils :as util]
    [clojure.contrib.io :as io]
    [clojure.contrib.str-utils2 :as su]
    [clj-http.client :as client])
  (:import
    [clojure.lang Sequential]
    [java.io InputStream]
    [java.net URL])
  (:gen-class))

(defn crawlable?
  [directives ^String path & {:keys [user-agent] :or {user-agent "*"}}]
  (let [permissions (filter #(= :disallow (first %))
                            (get directives user-agent))]
    (nil? (some #(.startsWith path (last %)) permissions))))

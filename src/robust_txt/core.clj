(ns robust-txt.core
  (:require
    [robust-txt.utils :as util]
    [clojure.contrib.io :as io]
    [clojure.contrib.str-utils2 :as su])
  (:gen-class))

(defn trim-comment
  "Strip out comments from a line."
  [line]
  (su/replace line #"#.*$" ""))

(defn parse-line [line]
  (let [[left right] (su/split (trim-comment line) #":" 2)]
    (if (nil? right)
      nil
      [(su/lower-case (su/trim left)) (su/trim right)])))

;(defn parse [lines]
;  (let [state (ref :start)
;        result (ref {})

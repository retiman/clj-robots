(ns robust-txt.test.core
  (:use
    [robust-txt.core]
    [robust-txt.utils :only (load-resource stream-to-string)]
    [clojure.test])
  (:require
    [clojure.contrib.str-utils2 :as su]))

(deftest test-parse-line
  (do
    (is (= ["user-agent" ":*:"]
           (parse-line "UsEr-AgEnt: :*:# This is a comment")))
    (is (nil? (parse-line "user-agent*")))
    (is (nil? (parse-line "")))))

(deftest test-parse-lines
  (let [stream (load-resource "robust_txt/test/robots.txt")
        string (stream-to-string stream)
        lines (su/split-lines string)
        result (parse-lines lines)]
    (println result)))

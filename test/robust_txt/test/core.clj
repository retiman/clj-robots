(ns robust-txt.test.core
  (:use
    [robust-txt.core]
    [robust-txt.utils :only (load-resource)]
    [clojure.test]))

(deftest test-parse-line
  (do
    (is (= ["user-agent" ":*:"]
           (parse-line "UsEr-AgEnt: :*:# This is a comment")))))

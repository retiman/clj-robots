(ns clj-robots.utils-test
  (:use
    [clj-robots.utils]
    [clj-robots.test.utils :only (refer-private)]
    [clojure.test])
  (:gen-class))

(refer-private 'clj-robots.utils)

(defn re-equals
  [a b]
  (= (.toString a) (.toString b)))

(deftest test-wildcard-to-regex
  (is (re-equals #"hel.o" (wildcard-to-regex "hel?o")))
  (is (re-equals #"hello.*" (wildcard-to-regex "hello*")))
  (is (re-equals #"hello\(there\)" (wildcard-to-regex "hello(there)"))))

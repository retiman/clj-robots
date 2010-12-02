(ns robust-txt.test.standard
  (:use
    [robust-txt.standard]
    [robust-txt.utils :only (load-resource stream-to-string)]
    [clojure.test])
  (:require
    [clojure.contrib.str-utils2 :as su]))

(deftest test-crawlable?
  (let [ds {"*" [[:disallow "/foo/"] [:disallow "/bar/"]]}]
    (is (crawlable? ds "/foo"))
    (is (crawlable? ds "/bif/"))
    (is (not (crawlable? ds "/bar/")))
    (is (not (crawlable? ds "/bar/2.html")))))

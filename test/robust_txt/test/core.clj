(ns robust-txt.test.core
  (:use
    [robust-txt.core]
    [robust-txt.utils :only (load-resource stream-to-string)]
    [clojure.test])
  (:require
    [clojure.contrib.str-utils2 :as su]))

(defn get-lines [resource]
  (let [stream (load-resource resource)
        string (stream-to-string stream)]
    (su/split-lines string)))

(deftest test-parse-line
  (do
    (is (= ["user-agent" ":*:"]
           (parse-line "UsEr-AgEnt: :*:# This is a comment")))
    (is (nil? (parse-line "user-agent*")))
    (is (nil? (parse-line "")))))

(deftest test-parse-lines
  (let [lines (get-lines "robust_txt/test/robots.txt")
        expected {"request-rate" 5
                  "crawl-delay" 10
                  "*"
                    [[:allow "/images/foo.jpg"]
                     [:disallow "/cgi-bin/"]
                     [:disallow "/images/"]
                     [:disallow "/tmp/"]
                     [:disallow "/private/"]]
                  "google"
                    [[:allow "/bif/baz/boo/"]
                     [:disallow "/moo/goo/too/"]]
                  "razzmatazz"
                    [[:disallow "/mif/tif/psd/"]
                     [:allow "/gif/png/img/"]]}
        result (parse-lines lines)]
    (is (= expected (dissoc result "modified-time")))))

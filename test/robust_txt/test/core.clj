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

(deftest test-trim-comment
  (is (= "hello there " (trim-comment "hello there #this is a comment!"))))

(deftest test-parse-line
  (do
    (is (= ["user-agent" ":*:"]
           (parse-line "UsEr-AgEnt: :*:# This is a comment")))
    (is (nil? (parse-line "user-agent*")))
    (is (nil? (parse-line "")))))

(deftest test-parse-lines
  (let [lines (get-lines "robust_txt/test/robots.txt")
        expected {:request-rate 5
                  :crawl-delay 10
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
        ds (parse-lines lines)
        result (dissoc ds :modified-time)]
    (do
      (is (contains? ds :modified-time))
      (is (= expected result)))))

(deftest test-parse-lines-no-content
  (is (= {"*" []}
         (dissoc (parse-lines [""]) :modified-time))))

(deftest test-crawlable-by-standard?
  (do
    (let [ds {"*" [[:disallow "/foo/"] [:disallow "/bar/"]]}]
      (is (crawlable-by-standard? ds "/foo"))
      (is (crawlable-by-standard? ds "/bif/"))
      (is (not (crawlable-by-standard? ds "/bar/")))
      (is (not (crawlable-by-standard? ds "/bar/2.html"))))
    (let [ds {"google" [[:disallow "/foo/"]]
              "*" [[:disallow "/bar/"]]}]
      (is (not (crawlable-by-standard? ds "/foo/" :user-agent "google")))
      (is (crawlable-by-standard? ds "/bar/" :user-agent "google"))
      (is (not (crawlable-by-standard? ds "/bar/" :user-agent "*")))
      (is (crawlable-by-standard? ds "/foo/" :user-agent "*"))
      (is (not (crawlable? ds "/bar/" :user-agent "google"))))))

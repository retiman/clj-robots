(ns robust-txt.test.core
  (:use
    [robust-txt.core]
    [robust-txt.utils :only (load-resource)]
    [clojure.test]))

(def keyvals
  (seq (vector {:allow "/images/foo.jpg"}
               {:allow "/images/bar.jpg"}
               {:allow "/css/main.css"}
               {:disallow "/cgi-bin"}
               {:disallow "/images/baz.jpg"}
               {:request-rate 100})))

(deftest test-merge-fn
  (is (= #{:x :y} (merge-fn :x :y)))
  (is (= #{:x :y} (merge-fn #{:x} #{:y})))
  (is (= #{:x :y} (merge-fn :x #{:y})))
  (is (= #{:x :y :z} (merge-fn #{:x :y} :z)))
  (is (= {:x #{1 2 3}} (merge-with merge-fn {:x 1} {:x 2} {:x 3}))))

(deftest test-trim-comment
  (is (= "hello there " (trim-comment "hello there #this is a comment!"))))

(deftest test-process-keyvals
  (let [expected {:request-rate 100
                  :disallow #{"/cgi-bin" "/images/baz.jpg"}
                  :allow #{"/images/foo.jpg"
                           "/images/bar.jpg"
                           "/css/main.css"}}]
    (is (= expected (process-keyvals keyvals)))))

(deftest test-process-robots-txt
  (let [stream (load-resource "robust_txt/test/robots.txt")
        directives (process-robots-txt stream)]
    (do
      (is (= 10 (directives :crawl-delay)))
      (is (= 10 (directives :request-rate)))
      (is (= #{"/cgi-bin/" "/images/" "/tmp/" "/private/"}
             (directives :disallow)))
      (is (= 5 (count directives)))))
  (let [s "User-agent: Mediapartners-Google\nDisallow: \n\nUser-agent: *\nDisallow: /search\n\nSitemap: http://www.jaydonnell.com/feeds/posts/default?orderby=updated\n"
        directives (process-robots-txt s)]
      (is (= #{"" "/search"} (directives :disallow)))))

(deftest test-process-robots-bad-txt
  (let [stream (load-resource "robust_txt/test/robots-bad.txt")
        directives (process-robots-txt stream)]
    (do
      (is (= 2 (count directives))
          (= #{"/foobar"} (directives :allow))))))

(deftest test-crawlable?
  (let [directives {:allowed #{"/foo" "/bar" "/baz"}
                    :disallowed #{"/foo/1" "/bif" "/bam"}}]
    (do
      (is (crawlable? directives "/foo"))
      (is (crawlable? directives "/foo/2"))
      (is (not (crawlable? directives "/foo/1"))))))


(defproject robust-txt "0.2.0a"
  :description "A robots.txt parser."
  :jar-dir "build"
  :compile-path "build/classes"
  :repositories
    {"clojars" "http://clojars.org/repo"
     "lousycoder" "http://maven.lousycoder.com"}
  :dependencies
    [[org.clojure/clojure "1.2.0"]
     [org.clojure/clojure-contrib "1.2.0"]
     [commons-io/commons-io "2.0"]
     [clj-http "0.1.1"]]
  :dev-dependencies
    [[lein-run "1.0.0"]])

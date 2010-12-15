(defproject robust-txt "0.3.1"
  :description "A robots.txt parser."
  :target-dir "build"
  :compile-path "build/classes"
  :repositories
    {"clojars" "http://clojars.org/repo"}
  :dependencies
    [[org.clojure/clojure "1.2.0"]
     [org.clojure/clojure-contrib "1.2.0"]
     [commons-io/commons-io "2.0"]
     [clj-http "0.1.2"]])

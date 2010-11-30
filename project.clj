(defproject robust-txt "0.2.2"
  :description "A robots.txt parser."
  :jar-dir "build"
  :compile-path "build/classes"
  :repositories
    {"clojars" "http://clojars.org/repo"}
  :dependencies
    [[org.clojure/clojure "1.2.0"]
     [org.clojure/clojure-contrib "1.2.0"]
     [commons-io/commons-io "2.0"]
     [clj-http "0.1.1"]]
  :dev-dependencies
    [[lein-run "1.0.0"]])

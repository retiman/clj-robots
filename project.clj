(defproject robust-txt "1.0.0-SNAPSHOT"
  :description "A robots.txt parser."
  :jar-dir "build"
  :compile-path "build/classes"
  :repositories
    {"clojars" "http://clojars.org/repo"
     "lousycoder" "http://maven.lousycoder.com"}
  :dependencies
    [[org.clojure/clojure "1.2.0"]
     [org.clojure/clojure-contrib "1.2.0"]
     [commons-io/commons-io "2.0"]]
  :dev-dependencies
    [[lein-run "1.0.0"]])

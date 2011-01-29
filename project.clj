(defproject clj-robots "0.4.2"
  :description "A robots.txt parser."
  :warn-on-reflection true
  :repositories
    {"clojars" "http://clojars.org/repo"}
  :dependencies
    [[org.clojure/clojure "1.2.0"]
     [org.clojure/clojure-contrib "1.2.0"]
     [commons-io/commons-io "2.0"]
     [clj-httpc "1.5.6"]]
  :dev-dependencies
    [[autodoc "0.7.1"]])

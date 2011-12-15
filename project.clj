(defproject clj-robots "0.6.0"
  :description "A robots.txt parser."
  :min-lein-version "1.6.2"
  :warn-on-reflection true
  :dependencies
    [[org.clojure/clojure "1.3.0"]
     [clj-time "0.3.3"]
     [commons-io "2.0"]]
  :dev-dependencies
    [[backtype/autodoc "0.9.0-SNAPSHOT"]
     [lein-clojars "0.7.0"]
     [robert/hooke "1.1.2"]
     [clj-http "0.2.6"]])

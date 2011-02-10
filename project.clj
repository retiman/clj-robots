(defproject clj-robots "0.5.0"
  :description "A robots.txt parser."
  :min-lein-version "1.4.2"
  :warn-on-reflection true
  :repositories
    {"clojars" "http://clojars.org/repo"}
  :dependencies
    [[org.clojure/clojure "1.2.0"]
     [org.clojure/clojure-contrib "1.2.0"]
     [commons-io/commons-io "2.0"]
     [clj-httpc "1.5.7"]]
  :dev-dependencies
    [[autodoc "0.7.1"]]
  :test-selectors
    {:default (fn [t] (not (:integration t)))
     :integration :integration
     :all (fn [_] true)})

(defproject clj-robots "0.5.1-1"
  :description "A robots.txt parser."
  :min-lein-version "1.4.2"
  :warn-on-reflection true
  :repositories
    {"clojars" "http://clojars.org/repo"}
  :dependencies
    [[org.clojure/clojure "1.2.0"]
     [org.clojure/clojure-contrib "1.2.0"]
     [clj-time "0.3.0"]
     [commons-io "2.0"]
     [clj-httpc "1.7.1-2"]]
  :dev-dependencies
    [[autodoc "0.7.1"]
     [lein-clojars "0.6.0"]
     [robert/hooke "1.1.0"]
     [swank-clojure "1.2.1"]]
  :test-selectors
    {:default (fn [t] (not (:integration t)))
     :integration :integration
     :all (fn [_] true)})

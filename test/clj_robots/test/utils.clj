(ns clj-robots.test.utils
  (:gen-class))

(defn refer-private
  "Access private symbols of a namespace."
  [ns]
  (doseq [[symbol var] (ns-interns ns)]
    (when (:private (meta var))
      (intern *ns* symbol var))))

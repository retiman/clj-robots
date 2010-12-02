(ns robust-txt.strategy.bing
  (:gen-class))

(defn crawlable?
  [directives path & {:keys [user-agent] :or {user-agent "*"}}]
  (throw (new UnsupportedOperationException "Method not implemented")))



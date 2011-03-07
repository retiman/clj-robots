(ns clj-robots.strategy.extended
  (:gen-class))

(defn crawlable?
  "Interprets robots.txt extended standard.

  See <http://www.conman.org/people/spc/robots2.html>"
  [directives path & {:keys [user-agent] :or {user-agent "*"}}]
  (let [select-disallows #(= :disallow (first %))
        permissions (filter select-disallows (mget directives user-agent))
        disallow-matches #(re-matches (utils/wildcard-to-regex (last %)) path)]
    (and (nil? (some disallow-matches permissions))
         (if (not= "*" user-agent)
           (crawlable? directives path :user-agent "*")
           true))))

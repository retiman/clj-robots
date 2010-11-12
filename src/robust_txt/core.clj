(ns robust-txt.core
  (:require
    [clojure.contrib.io :as io]
    [clojure.contrib.str-utils2 :as s])
  (:gen-class))

(def int-keys #{:crawl-delay :request-rate})

(def set-keys #{:allow :disallow})

(defn merge-fn
  "Merges a and b into a flattened set."
  [a b]
  (let [to (if (set? a) a #{a})
        from (if (set? b) b #{b})]
    (into to from)))

(defn trim-comment
  "Strip out comments from a line in robots.txt."
  [s]
  (s/replace s #"#.*$" ""))

(defn make-pair [k v]
  (if (or (= "" k) (= "" v))
    nil
    (hash-map k v)))

(defn process-lines
  "Process the lines of the robots.txt file into key/value pairs."
  [lines]
  (let [uncommented-lines (map trim-comment lines)
        split-lines (map #(s/split % #":" 2) uncommented-lines)]
    (filter #(= (count %) 2) split-lines)))

(defn process-pair
  "Process a key/value pair."
  [pair]
  (try
    (let [k (keyword (s/lower-case (s/trim (first pair))))
          t (s/trim (last pair))
          v (cond
              (contains? int-keys k) (Integer/parseInt t)
              (contains? set-keys k) #{t}
              :default t)]
      (make-pair k v))
    (catch NumberFormatException e nil)))

(defn process-pairs
  "Create a hashmap from key/value pairs."
  [pairs]
  (filter #(not (nil? %)) (map process-pair pairs)))

(defn process-keyvals
  "Merge multiple key/value pairs into sets."
  [keyvals]
  (apply merge-with (cons merge-fn keyvals)))

(defmulti process-robots-txt class)

(defmethod process-robots-txt java.io.InputStream [stream]
    (process-robots-txt (ru/stream-to-string stream)))

(defmethod process-robots-txt String [s]
  (let [lines (s/split-lines s)
        pairs (process-lines lines)
        keyvals (process-pairs pairs)]
    (process-keyvals keyvals)))

(comment Google's crawler evaluates all allowed patterns, and then it processes
         all disallowed patterns.  The same strategy is used here.)
(defn crawlable?
  "Returns true if this URL should be crawled."
  [directives url]
  (let [allowed? (matches-url? directives :allowed url)
        disallowed? (matches-url? directives :disallowed url)]
    (and allowed? (not disallowed?))))

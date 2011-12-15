{:namespaces
 ({:source-url nil,
   :wiki-url "clj-robots.core-api.html",
   :name "clj-robots.core",
   :doc nil}
  {:source-url nil,
   :wiki-url "clj-robots.strategy.bing-api.html",
   :name "clj-robots.strategy.bing",
   :doc nil}
  {:source-url nil,
   :wiki-url "clj-robots.strategy.extended-api.html",
   :name "clj-robots.strategy.extended",
   :doc nil}
  {:source-url nil,
   :wiki-url "clj-robots.strategy.google-api.html",
   :name "clj-robots.strategy.google",
   :doc nil}
  {:source-url nil,
   :wiki-url "clj-robots.utils-api.html",
   :name "clj-robots.utils",
   :doc nil}),
 :vars
 ({:arglists
   ([directives path & {:keys [user-agent], :or {user-agent "*"}}]),
   :name "crawlable?",
   :namespace "clj-robots.core",
   :source-url nil,
   :raw-source-url nil,
   :wiki-url "/clj-robots.core-api.html#clj-robots.core/crawlable?",
   :doc
   "Returns true if a list of directives allows the path to be crawled using\nthis interpretation of robots.txt:\n\nhttp://www.robotstxt.org/\n\nNote that allow directives are completely ignored and only the first\ndisallow directive is consulted to determine if a path can be crawled.",
   :var-type "function",
   :line 133,
   :file "src/clj_robots/core.clj"}
  {:file "src/clj_robots/core.clj",
   :raw-source-url nil,
   :source-url nil,
   :wiki-url "/clj-robots.core-api.html#clj-robots.core/get-url",
   :namespace "clj-robots.core",
   :line 121,
   :var-type "var",
   :doc
   "Returns the robots.txt URL for a particular host (given a URL).",
   :name "get-url"}
  {:file "src/clj_robots/core.clj",
   :raw-source-url nil,
   :source-url nil,
   :wiki-url "/clj-robots.core-api.html#clj-robots.core/parse",
   :namespace "clj-robots.core",
   :line 149,
   :var-type "var",
   :doc
   "Parse robots.txt; returns a data structure to pass to crawlable?",
   :name "parse"}
  {:arglists ([resource]),
   :name "get-lines",
   :namespace "clj-robots.utils",
   :source-url nil,
   :raw-source-url nil,
   :wiki-url "/clj-robots.utils-api.html#clj-robots.utils/get-lines",
   :doc
   "Load a resource, convert it to a string, and return a vector of lines.",
   :var-type "function",
   :line 45,
   :file "src/clj_robots/utils.clj"}
  {:arglists ([path]),
   :name "load-resource",
   :namespace "clj-robots.utils",
   :source-url nil,
   :raw-source-url nil,
   :wiki-url
   "/clj-robots.utils-api.html#clj-robots.utils/load-resource",
   :doc "Return a resource located on path.",
   :var-type "function",
   :line 10,
   :file "src/clj_robots/utils.clj"}
  {:arglists ([s]),
   :name "parse-int",
   :namespace "clj-robots.utils",
   :source-url nil,
   :raw-source-url nil,
   :wiki-url "/clj-robots.utils-api.html#clj-robots.utils/parse-int",
   :doc "Convert a String to an Integer.",
   :var-type "function",
   :line 50,
   :file "src/clj_robots/utils.clj"}
  {:arglists ([stream]),
   :name "stream-to-string",
   :namespace "clj-robots.utils",
   :source-url nil,
   :raw-source-url nil,
   :wiki-url
   "/clj-robots.utils-api.html#clj-robots.utils/stream-to-string",
   :doc "Convert an InputStream to a String.",
   :var-type "function",
   :line 17,
   :file "src/clj_robots/utils.clj"}
  {:arglists ([text]),
   :name "wildcard-to-regex",
   :namespace "clj-robots.utils",
   :source-url nil,
   :raw-source-url nil,
   :wiki-url
   "/clj-robots.utils-api.html#clj-robots.utils/wildcard-to-regex",
   :doc
   "Convert a wildcard pattern to a Java Pattern for matching paths.\nNote that .* is added to the end of the pattern for this reason.",
   :var-type "function",
   :line 25,
   :file "src/clj_robots/utils.clj"})}

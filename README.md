DESCRIPTION
===========
A robots.txt parser.  See <http://www.robotstxt.org/>.  Support for different
interpretations forthcoming.

USAGE
=====
Here is an example of the library's use.

    (require '[clojure.contrib.io :as io])
    (use 'robust-txt.core)
    (def robots (get-robots (io/as-url "http://www.lousycoder.com")))
    (crawlable? robots "/cgi-bin" :user-agent "*")
    (crawlable? robots "/images" :user-agent "googlebot")
    (crawlable? robots "/admin") ; Assume user-agent is *

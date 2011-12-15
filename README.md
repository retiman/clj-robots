DESCRIPTION
===========
A robots.txt parser.  See <http://www.robotstxt.org/>.  Support for different
interpretations forthcoming.

CHANGES
=======

0.6.0
-----
* Removed deprecated API.
* Removed dependency on [clj-httpc](https://github.com/retiman/clj-httpc) library.
* Removed `get` API; use [clj-http](https://github.com/dakrone/clj-http) or a different library for your HTTP requests.

USAGE
=====
To use, include this in your Clojure program:

    (require '[clj-http.client :as client])
    (require '[clj-robots.core :as robots])

Save robots.txt from a website:

    (def robots
      ((comp robots/parse
             #(get % :body)
             client/get)
         "http://www.google.com/robots.txt"))
    -> #'user/robots

Now check if any paths are crawlable:

    (crawlable? robots "/search")
    -> false

    (crawlable? robots "/news" :user-agent "*")
    -> false

    (crawlable? robots "/jsapi")
    -> true

Examples of other usage:

    (:sitemap robots)
    -> ["http://www.gstatic.com/s2/sitemaps/profiles-sitemap.xml" "http://www.google.com/hostednews/sitemap_index.xml" "http://www.google.com/ventures/sitemap_ventures.xml" "http://www.google.com/sitemaps_webmasters.xml" "http://www.gstatic.com/trends/websites/sitemaps/sitemapindex.xml" "http://www.gstatic.com/dictionary/static/sitemaps/sitemap_index.xml"]

    (:modified-time robots)
    -> 1297291259732

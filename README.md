DESCRIPTION
===========
A robots.txt parser.  See <http://www.robotstxt.org/>.  Support for different
interpretations forthcoming.

USAGE
=====
To use, include this in your Clojure program:

    (use 'clj-robots.core)

Save robots.txt from a website:

    (def robots
      (parse-robots (get-robots (io/as-url "http://www.lousycoder.com"))))
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

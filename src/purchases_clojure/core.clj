(ns purchases-clojure.core
  (:require [clojure.string :as s]
            [clojure.walk :as w]
            [ring.adapter.jetty :as j]
            [hiccup.core :as h])
  (:gen-class))

(defn purchases-html []
  (println "Type a category and hit enter.")
  (let [purchases (slurp "purchases.csv")
        purchases (s/split-lines purchases)
        purchases (map (fn [line]
                         (s/split line #","))
                       purchases)
        header (first purchases)
        purchases (rest purchases)
        purchases (map (fn [line]
                         (interleave header line))
                       purchases)
        purchases (map (fn [line]
                         (apply hash-map line))
                       purchases)
        purchases (w/keywordize-keys purchases)
        input (read-line)
        purchases (filter (fn [line]
                            (= input (:category line)))
                          purchases)]
    (spit "filtered_purchases.edu"
          (pr-str purchases))
    purchases))

(defn handler [request]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (h/html [:html
                     [:body
                      [:a {:href "http://reddit.com"}
                       "Reddit"]
                      [:br]
                      (purchases-html)]])})

(defn -main [& args]
  (j/run-jetty #'handler {:port 3000 :join? false}))

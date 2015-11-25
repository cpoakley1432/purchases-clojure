(ns purchases-clojure.core
  (:require [clojure.string :as s]
            [clojure.walk :as w]
            [clojure.pprint :as pp])
  (:gen-class))

(defn -main [& args]
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
          (with-out-str (pp/pp-newline purchases)))))

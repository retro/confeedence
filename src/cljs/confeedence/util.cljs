(ns confeedence.util
  (:require [clojure.string :as str]))

(defn get-access-token [app-db]
  (get-in app-db [:kv :access-token]))

(defn format-date [datetime]
  (let [date (first (str/split datetime "T"))
        [y m d] (str/split date "-")]
    (str m "/" d "/" y)))

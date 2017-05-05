(ns confeedence.datasources
  (:require [hodgepodge.core :refer [get-item local-storage]]
            [confeedence.util.dataloader :refer [map-loader]]))

(def access-token-loader 
  (map-loader
   (fn [_]
     (get-item local-storage "whenhub-access-token"))))

(def datasources
  {})

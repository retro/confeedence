(ns confeedence.stylesheets.typography
  (:require [confeedence.stylesheets.colors :refer [colors]]))

(defn stylesheet []
  [:.c-title {:font-family "'Open Sans', sans-serif"
              :font-weight 800
              :font-size "2rem"}
   :.c-subtitle {:font-family "'Open Sans', sans-serif"
                 :font-weight 700
                 :font-size "1.5rem"}
   :.c-emphasized {:font-family "'Open Sans', sans-serif"
                   :font-weight 600
                   :font-size "1.25rem"}
   :.c-body {:font-family "'Raleway', sans-serif"
             :font-weight 400
             :font-size "1rem"}
   :h1 {:font-family "'Open Sans', sans-serif"
        :font-weight 800
        :font-size "2rem"}
   :h2 {:font-family "'Open Sans', sans-serif"
                 :font-weight 700
                 :font-size "1.5rem"}
   :h3 {:font-family "'Open Sans', sans-serif"
        :font-weight 600
        :font-size "1.25rem"}])

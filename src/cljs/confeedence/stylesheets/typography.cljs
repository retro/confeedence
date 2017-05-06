(ns confeedence.stylesheets.typography
  (:require [confeedence.stylesheets.colors :refer [colors]]))

(defn stylesheet []
  [[:.c-title {:font-family "'Open Sans', sans-serif"
               :font-weight 800
               :font-size "4rem"}]
   [:.c-subtitle {:font-family "'Open Sans', sans-serif"
                  :font-weight 700
                  :font-size "3.25rem"}]
   [:.c-emphasized {:font-family "'Open Sans', sans-serif"
                    :font-weight 600
                    :font-size "2.75rem"}]
   [:.c-button {:font-family "'Open Sans', sans-serif"
                :font-weight 800
                :font-size "1.5rem"
                :line-height "1.75rem"}]
   [:.c-body {:font-family "'Raleway', sans-serif"
              :font-weight 400
              :font-size "1.6rem"}]
   [:.c-link {:font-family "'Raleway', sans-serif"
              :font-weight 200
              :font-size "2.5rem"}]
   [:.c-large {:font-family "'Raleway', sans-serif"
              :font-weight 400
              :font-size "2.4rem"}]
   [:.c-medium {:font-family "'Raleway', sans-serif"
              :font-weight 400
              :font-size "1.75rem"}]
   [:.c-small {:font-family "'Raleway', sans-serif"
              :font-weight 400
              :font-size "1.4rem"}]
   [:h1 {:font-family "'Open Sans', sans-serif"
         :font-weight 800
         :font-size "4rem"}]
   [:h2 {:font-family "'Open Sans', sans-serif"
         :font-weight 700
         :font-size "3.25rem"}]
   [:h3 {:font-family "'Open Sans', sans-serif"
         :font-weight 600
         :font-size "2.5rem"}]
   [:.uppercase {:text-transform "uppercase"}]
   [:.pointer {:cursor "pointer"}]])

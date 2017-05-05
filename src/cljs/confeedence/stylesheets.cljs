(ns confeedence.stylesheets
  (:require [garden-basscss.core :as core]
            [garden-basscss.vars :refer [vars]]
            [confeedence.stylesheets.colors :as colors]
            [confeedence.stylesheets.btn :as btn]
            [garden.core :as garden :refer [at-media]]
            [garden.units :refer [em rem px px-]]
            [garden.stylesheet :refer [at-media]]
            [confeedence.stylesheets.colors :refer [colors-with-variations]]
            [confeedence.stylesheets.typography :as typography]
            [keechma.toolbox.css.core :as toolbox-css])
  (:require-macros [garden.def :refer [defkeyframes]]))

(def system-font-stack
  "-apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue', sans-serif")

(def system-font-stack-monospace
  "'Menlo', 'Monaco', 'Consolas', 'Lucida Console', 'Lucida Sans Typewriter', 'Andale Mono', 'Courier New', monospaced")

(defkeyframes spin
  [:100% {:transform "rotate(-360deg)"}])

(swap! vars assoc :spaces {0 0
                           1 (rem 1)
                           2 (rem 2)
                           3 (rem 3)
                           4 (rem 4)
                           5 (rem 5)
                           6 (rem 6)
                           "0-5" (rem 0.5)
                           "1-5" (rem 1.5)
                           "2-5" (rem 2.5)
                           "3-5" (rem 3.5)
                           "4-5" (rem 4.5)
                           "5-5" (rem 5.5)
                           "6-5" (rem 6.5)})

(defn stylesheet []
  [[:* {:box-sizing 'border-box}]
   [:html {:font-size "62.5%"
           :height "100%"}]
   [:#app {:height "100%"}]
   [:body {:margin 0
           :font-family system-font-stack
           :font-size "16px"
           :height "100%"
           :text-rendering "optimizeLegibility"
           :-webkit-font-smoothing "antialiased"
           :-moz-osx-font-smoothing "grayscale"}]
   [:img {:max-width "100%"}]
   [:svg {:max-width "100%"}]
   [:table {:width "100%"
            :border-spacing 0}]
   (core/stylesheet)
   (btn/stylesheet)
   (colors/stylesheet)
   [:.monospaced {:font-family system-font-stack-monospace}]
   (typography/stylesheet)
   [:.height-full {:height "100%"}]
   [:.width-full {:width "100%"}]
   [:.cursor-pointer {:cursor 'pointer}] 
   [:.pill {:border-radius "999em"}]
   @toolbox-css/component-styles
   spin
   [:.spin {:animation [[spin "1.5s" :linear :infinite]]}]])

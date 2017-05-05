(ns confeedence.ui.pages.homepage
  (:require [keechma.ui-component :as ui]))

(defn render [ctx]
  [:div "HOMEPAGE"])

(def component
  (ui/constructor {:renderer render}))

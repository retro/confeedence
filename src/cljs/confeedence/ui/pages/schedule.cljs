(ns confeedence.ui.pages.schedule
  (:require [keechma.ui-component :as ui]))

(defn render [ctx]
  [:div "SCHEDULE"])

(def component
  (ui/constructor {:renderer render}))

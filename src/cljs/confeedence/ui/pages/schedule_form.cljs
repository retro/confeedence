(ns confeedence.ui.pages.schedule-form
  (:require [keechma.ui-component :as ui]))

(defn render [ctx]
  [:div "SCHEDULE FORM"])

(def component
  (ui/constructor {:renderer render}))

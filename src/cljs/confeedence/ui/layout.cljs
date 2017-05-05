(ns confeedence.ui.layout
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [route>]]))

(defn render [ctx]
  [:div "LAYOUT"
   (case (:page (route> ctx))
     "homepage" [(ui/component ctx :page-homepage)]
     "show"     [(ui/component ctx :page-schedule)]
     "edit"     [(ui/component ctx :page-schedule-form)])])

(def component
  (ui/constructor {:renderer render
                   :component-deps [:page-homepage :page-schedule :page-schedule-form]}))

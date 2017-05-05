(ns confeedence.ui.layout
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [route> sub>]]
            [confeedence.ui.components.pure.spinner :refer [spinner]]))

(defn loading-access-token? [ctx]
  (let [status (:status (sub> ctx :access-token-meta))]
    (or (nil? status) (= :pending status))))

(defn render [ctx]
  
  (if (loading-access-token? ctx)
    [:div.flex.justify-center.items-center.height-full
     [spinner 64 "#ff3300"]]
    [:div.flex.flex-column.height-full
     [:div.border-bottom.bd-grey "LAYOUT"]
     [:div.flex-grow.flex
      (case (:page (route> ctx))
        "homepage" [(ui/component ctx :page-homepage)]
        "show"     [(ui/component ctx :page-schedule)]
        "edit"     [(ui/component ctx :page-schedule-form)]
        nil)]]))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:access-token-meta]
                   :component-deps [:page-homepage :page-schedule :page-schedule-form]}))

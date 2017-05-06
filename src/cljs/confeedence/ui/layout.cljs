(ns confeedence.ui.layout
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.css.core :refer [defelement]]
            [keechma.toolbox.ui :refer [route> sub>]]
            [confeedence.ui.components.pure.spinner :refer [spinner]]))

(defelement spinner-wrap
  :class [:flex :justify-center :items-center :height-full])

(defelement layout-wrap
  :class [:flex :flex-column :height-full])

(defelement title-wrap
  :class [:border-bottom :bd-grey])

(defelement page-wrap
  :class [:flex-grow :flex])

(defn loading-access-token? [ctx]
  (let [status (:status (sub> ctx :access-token-meta))]
    (or (nil? status) (= :pending status))))

(defn render [ctx]
  
  (if (loading-access-token? ctx)
    [spinner-wrap
     [spinner 64 "#ff3300"]]
    [layout-wrap
     [title-wrap "LAYOUT"]
     [page-wrap
      (case (:page (route> ctx))
        "homepage" [(ui/component ctx :page-homepage)]
        "show"     [(ui/component ctx :page-schedule)]
        "edit"     [(ui/component ctx :page-schedule-form)]
        nil)]]))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:access-token-meta]
                   :component-deps [:page-homepage :page-schedule :page-schedule-form]}))

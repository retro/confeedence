(ns confeedence.ui.pages.schedule-form
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub> route> <cmd]]
            [keechma.toolbox.css.core :refer-macros [defelement]]
            [confeedence.stylesheets.colors :refer [colors-with-variations]]))

(defelement -sidebar-form-wrap
  :style [{:width "400px"
           :border-left (str "2px solid " (:grey colors-with-variations))}
          ])

(defn render [ctx]
  (let [access-token (sub> ctx :access-token)
        current-route (route> ctx)
        id (:id current-route)
        form (:form current-route)]
    [:div.flex-grow.flex
     (if access-token
       (if id
         [:div.flex-grow.flex
          [(ui/component ctx :schedule)]
          (when form
            [-sidebar-form-wrap
             (case (:type form)
               "conference" [(ui/component ctx :form-schedule)]
               nil)])]
         [:div.flex-grow
          [(ui/component ctx :schedule-list)]
          [(ui/component ctx :form-schedule)]])
       [(ui/component ctx :form-access-token)])]))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:access-token :current-schedule]
                   :component-deps [:form-access-token
                                    :form-schedule
                                    :schedule-list
                                    :schedule]}))

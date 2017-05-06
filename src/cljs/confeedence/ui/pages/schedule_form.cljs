(ns confeedence.ui.pages.schedule-form
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub> route> <cmd]]
            [keechma.toolbox.css.core :refer-macros [defelement]]
            [confeedence.stylesheets.colors :refer [colors-with-variations]]))

(defelement -sidebar-form-wrap
  :style [{:min-width "400px"
           :overflow-y "auto"
           :border-left (str "2px solid " (:grey colors-with-variations))}
          ])

(defelement content-inner-wrap
  :class [:flex-auto :flex]
  :style [{:max-width "100vw"
           :overflow "hidden"}])

(defn render [ctx]
  (let [access-token (sub> ctx :access-token)
        current-route (route> ctx)
        id (:id current-route)
        form (:form current-route)]
    [:div.flex-auto.flex
     (if access-token
       (if id
         [content-inner-wrap
          [(ui/component ctx :schedule)]
          (when form
            [-sidebar-form-wrap
             [:a {:href (ui/url ctx (dissoc current-route :form))} "Close Form"]
             (case (:type form)
               "conference" [(ui/component ctx :form-schedule)]
               "event" [(ui/component ctx :form-event)]
               "news" [(ui/component ctx :form-news)]
               "talk" [(ui/component ctx :form-talk)]
               nil)])]
         [:div.flex-auto.flex
          [(ui/component ctx :schedule-list)]
          [(ui/component ctx :form-schedule)]])
       [(ui/component ctx :form-access-token)])]))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:access-token :current-schedule]
                   :component-deps [:form-access-token
                                    :form-schedule
                                    :form-event
                                    :form-news
                                    :form-talk
                                    :schedule-list
                                    :schedule]}))

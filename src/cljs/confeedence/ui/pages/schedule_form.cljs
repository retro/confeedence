(ns confeedence.ui.pages.schedule-form
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer [sub> route> <cmd]]
            [keechma.toolbox.css.core :refer-macros [defelement]]
            [confeedence.stylesheets.colors :refer [colors-with-variations]]
            [confeedence.ui.components.pure.spinner :refer [spinner]]))

(defelement spinner-wrap
  :class [:flex :flex-auto :justify-center :items-center]
  :style {:min-height "100%"})

(defelement -sidebar-form-wrap
  :class [:relative]
  :style [{:min-width "400px"
           :overflow-y "auto"
           :border-left (str "2px solid " (:grey colors-with-variations))}
          ])

(defelement content-inner-wrap
  :class [:flex-auto :flex]
  :style [{:max-width "100vw"
           :overflow "hidden"}])

(defelement close-wrap
  :tag :a
  :class [:absolute :left-0 :right-0]
  :style [{:width "4rem"
           :height "4rem"}])

(defelement close-icon
  :tag :img
  :class [:fit]
  :style [{:width "4rem"
           :height "4rem"}])

(defn render [ctx]
  (let [access-token (sub> ctx :access-token)
        current-route (route> ctx)
        id (:id current-route)
        form (:form current-route)]

    (if (or (= :pending (:status (sub> ctx :current-schedule-meta)))
            (= :pending (:status (sub> ctx :current-schedule-events-meta))))
      [spinner-wrap
       [spinner 64 "#ff3300"]]

      [:div.flex-auto.flex
       (if access-token
         (if id
           [content-inner-wrap
            [(ui/component ctx :schedule)]
            (when form
              [-sidebar-form-wrap
               [close-wrap {:href (ui/url ctx (dissoc current-route :form))}
                [close-icon {:src "/images/close.svg"}]]
               (case (:type form)
                 "conference" [(ui/component ctx :form-schedule)]
                 "event" [(ui/component ctx :form-event)]
                 "news" [(ui/component ctx :form-news)]
                 "talk" [(ui/component ctx :form-talk)]
                 nil)])]
           [:div.flex-auto.flex
            [(ui/component ctx :schedule-list)]
            [(ui/component ctx :form-schedule)]])
         [(ui/component ctx :form-access-token)])])))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:access-token
                                       :current-schedule
                                       :current-schedule-meta
                                       :current-schedule-events-meta]
                   :component-deps [:form-access-token
                                    :form-schedule
                                    :form-event
                                    :form-news
                                    :form-talk
                                    :schedule-list
                                    :schedule]}))

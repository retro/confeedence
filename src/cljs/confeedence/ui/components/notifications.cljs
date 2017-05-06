(ns confeedence.ui.components.notifications
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.ui :refer (sub> <cmd)]
            [keechma.toolbox.css.core :refer-macros [defelement]]))

(defelement -notifications-wrap
  :class [:fixed :bottom-0 :left-0 :pb4 :pl4]
  :style {:width "350px"})

(defelement -notification
  :class [:rounded :bg-green :bg-h-green-hover :c-white :p2 :mt2 :cursor-pointer]
  :style [{:box-shadow "0 0 10px 0 rgba(0,0,0,0.3)"}])

(defn render [ctx]
  (let [notifications (sub> ctx :notifications)]
    [-notifications-wrap
     (doall (map (fn [n]
                   [-notification
                    {:key (:id n) :on-click #(<cmd ctx :remove (:id n))}
                    (:message n)]) notifications))]))

(def component
  (ui/constructor {:renderer render
                   :topic :notifications
                   :subscription-deps [:notifications]}))

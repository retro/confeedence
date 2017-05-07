(ns confeedence.ui.components.schedule-list
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.css.core :refer-macros [defelement]]
            [keechma.toolbox.ui :refer (sub>)]))

(defelement -conference-wrap
  :class [:border-right :bd-lighter-gray]
  :style [])

(defelement -title
  :tag :h3
  :class [:c-white :bg-green :m0 :px1 :py2])

(defelement -conference-list-wrap
  :tag :ul
  :class [:list-reset :m0])

(defelement -conference-list-item
  :tag :li
  :class [:border-top :bd-lighter-gray :bg-h-lighter-gray :flex])

(defelement -link
  :tag :a
  :class [:text-decoration-none :c-body :c-dark :p1 :flex-auto :pointer])

(defelement -no-conferences
  :class [:bg-dark :c-white :p2 :c-body])

(defn render [ctx]
  (let [schedules (sub> ctx :schedules)
        loading? (= :pending (:status (sub> ctx :schedules-meta)))]
    (when-not loading?
      (if (seq schedules)
        [-conference-wrap
         [-title "Created Conferences"]
         [-conference-list-wrap
          (doall (map-indexed (fn [i,s]
                                [-conference-list-item {:key (:id s)
                                                       :class (when (= (inc i) (count schedules)) "border-bottom")}
                                 [-link {:href (ui/url ctx {:page "edit" :id (:id s)})}
                                  (:name s)]])
                              schedules))]]
        [-no-conferences "There are no created schedules"]))))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:schedules :schedules-meta]}))

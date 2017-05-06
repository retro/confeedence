(ns confeedence.ui.layout
  (:require [keechma.ui-component :as ui]
            [keechma.toolbox.css.core :refer-macros [defelement]]
            [keechma.toolbox.ui :refer [route> sub>]]
            [confeedence.ui.components.pure.spinner :refer [spinner]]
            [clojure.string :as str]
            [confeedence.util :refer [format-date]]))

(defelement spinner-wrap
  :class [:flex :justify-center :items-center :height-full])

(defelement layout-wrap
  :class [:flex :flex-column :height-full])

(defelement navbar-wrap
  :class [:absolute :z1 :border-bottom :bd-light-gray :bg-lightest-gray :px2 :flex :items-center :justify-start :relative]
  :style [{:height "78px"
           :width "100vw"
           :top 0}])

(defelement page-wrap
  :class [:flex-auto :flex]
  :style [{:margin-top "78px"}])

(defelement menu
  :tag :ul
  :class [:list-reset :flex :flex-row :m0])

(defelement menu-item
  :tag :li
  :class [:mr2])

(defelement menu-item-link
  :tag :a
  :class [:text-decoration-none :c-link :c-semi-gray :c-h-dark :pointer :uppercase])

(defelement profile-wrap
  :class [:absolute :right-0 :flex :flex-row :items-center]
  :style [{:height "100%"}])

(defelement profile-greeting
  :class [:c-body :c-semi-gray :mr1 :xs-hide])

(defelement profile-greeting-mobile
  :class [:c-small :c-semi-gray :my1 :sm-hide :md-hide :lg-hide])

(defelement profile-img
  :tag :img
  :class [:circle :fit :pointer :relative ]
  :style [{:width "5rem"
           :height "5rem"}])

(defelement profile-details
  :class [:flex :flex-column :items-center :pointer :pr2]
  :style [[:&:hover>div {:top "6.4rem"
                         :visibility "visible"}]])

(defelement profile-links-container
  :class [:absolute :bg-lightest-gray :right-0]
  :style [{:width "25rem"
           :visibility "hidden"}])

(defelement spacer
  :class [:block :border-bottom :bd-light-gray]
  :style [{:height "1.4rem"
           :width "100%"}])

(defelement profile-links-wrap
  :tag :ul
  :class [:list-reset :flex :flex-column :m0 :border :bd-light-gray])

(defelement link-item-wrap
  :tag :li
  :class [:px2 :my1])

(defelement link-item
  :tag :a
  :class [:text-decoration-none :c-body :c-semi-gray :c-h-dark :pointer])

(defelement profile-info-wrap
  :class [:p2 :border-left :border-right :bd-light-gray])

(defelement profile-info
  :tag :p
  :class [:c-small :c-semi-gray :my0-5 :italic])

(defelement logo-wrap
  :class [:mr6 :py1]
  :style [{:height "100%"}])

(defelement logo
  :tag :img
  :class [:fit]
  :style [{:height "100%"}])

(defn loading-access-token? [ctx]
  (let [status (:status (sub> ctx :access-token-meta))]
    (or (nil? status) (= :pending status))))

(defn render [ctx]
  (let [usr (sub> ctx :current-user)]
    (if (loading-access-token? ctx)
      [spinner-wrap
       [spinner 64 "#ff3300"]]
      [layout-wrap
       [navbar-wrap
        [logo-wrap
          [logo {:src "/images/logo.svg"}]]
        [menu 
         [menu-item 
          [menu-item-link {:href (ui/url ctx {:page "home"})} "Home"]]
         [menu-item
          [menu-item-link {:href (ui/url ctx {:page "edit"})} "Admin"]]]
        (when usr
          [profile-wrap
           [profile-greeting (str "Hello " (get-in usr [:name :givenName]) ".")]
           [profile-details
            [profile-img {:src (:photo usr)}]
            [profile-links-container
             [spacer]
             [profile-info-wrap
              [profile-greeting-mobile (str "Hello " (get-in usr [:name :givenName]) ".")]
              [profile-info (:email usr)]
              [profile-info (str "Member since " (format-date (:createdAt usr)))]]
             [profile-links-wrap
              [link-item-wrap
               [link-item "My events"]]
              [link-item-wrap 
               [link-item "Logout"]]]]]])]
       [page-wrap
        (case (:page (route> ctx))
          "homepage" [(ui/component ctx :page-homepage)]
          "show"     [(ui/component ctx :page-schedule)]
          "edit"     [(ui/component ctx :page-schedule-form)]
          nil)]])))

(def component
  (ui/constructor {:renderer render
                   :subscription-deps [:access-token-meta :current-user]
                   :component-deps [:page-homepage :page-schedule :page-schedule-form]}))

(ns confeedence.ui
  (:require [confeedence.ui.layout :as layout]
            [confeedence.ui.pages.homepage :as page-homepage]
            [confeedence.ui.pages.schedule :as page-schedule]
            [confeedence.ui.pages.schedule-form :as page-schedule-form]
            [confeedence.ui.components.forms.access-token :as form-access-token]
            [confeedence.ui.components.forms.schedule :as form-schedule]
            [confeedence.ui.components.forms.event :as form-event]
            [confeedence.ui.components.forms.news :as form-news]
            [confeedence.ui.components.forms.talk :as form-talk]
            [confeedence.ui.components.schedule-list :as schedule-list]
            [confeedence.ui.components.schedule :as schedule]
            [confeedence.ui.components.notifications :as notifications]))

(def ui
  {:main               layout/component

   :page-homepage      page-homepage/component
   :page-schedule      page-schedule/component
   :page-schedule-form page-schedule-form/component

   :form-access-token  form-access-token/component
   :form-schedule      form-schedule/component
   :form-event         form-event/component
   :form-news          form-news/component
   :form-talk          form-talk/component

   :schedule-list      schedule-list/component
   :schedule           schedule/component

   :notifications      notifications/component})

(ns confeedence.ui
  (:require [confeedence.ui.layout :as layout]
            [confeedence.ui.pages.homepage :as page-homepage]
            [confeedence.ui.pages.schedule :as page-schedule]
            [confeedence.ui.pages.schedule-form :as page-schedule-form]))

(def ui
  {:main               layout/component
   :page-homepage      page-homepage/component
   :page-schedule      page-schedule/component
   :page-schedule-form page-schedule-form/component})

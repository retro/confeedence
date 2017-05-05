(ns confeedence.controllers
  (:require [keechma.toolbox.dataloader.controller :as dataloader-controller]
            [keechma.toolbox.forms.controller :as forms-controller]
            [keechma.toolbox.forms.mount-controller :as forms-mount-controller]
            [confeedence.forms :as forms]
            [confeedence.datasources :refer [datasources]]
            [confeedence.edb :refer [edb-schema]]))

(def controllers
  (-> {}
      (forms-controller/register forms/forms)
      (forms-mount-controller/register forms/forms-params)
      (dataloader-controller/register datasources edb-schema)))

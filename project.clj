(defproject confeedence "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.229"]
                 [reagent "0.6.1"]
                 [keechma "0.2.0-SNAPSHOT-11" :exclusions [cljsjs/react-with-addons]]
                 [garden "1.3.2"]
                 [funcool/promesa "1.6.0"]
                 [keechma/forms "0.1.2"]
                 [medley "0.8.4"]
                 [cljs-ajax "0.5.8"]
                 [com.stuartsierra/dependency "0.2.0"]
                 [keechma/entitydb "0.1.0"]
                 [org.clojars.mihaelkonjevic/garden-basscss "0.2.0"]
                 [binaryage/devtools "0.8.2"]
                 [garden "1.3.2"]
                 [hodgepodge "0.1.3"]
                 [cljsjs/react-datetime "2.6.0-0" :exclusions [cljsjs/react]]
                 [cljsjs/moment "2.17.1-0"]
                 [cljsjs/moment-timezone "0.5.11-0"]
                 [keechma/toolbox "0.0.1-SNAPSHOT-2"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :plugins [[lein-cljsbuild "1.1.4"]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "target"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :profiles
  {:dev
   {:dependencies []

    :plugins      [[lein-figwheel "0.5.10"]]
    }}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "confeedence.core/reload"}
     :compiler     {:main                 confeedence.core
                    :optimizations        :none
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/dev"
                    :asset-path           "js/compiled/dev"
                    :source-map-timestamp true}}

    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            confeedence.core
                    :optimizations   :advanced
                    :output-to       "resources/public/js/compiled/app.js"
                    :output-dir      "resources/public/js/compiled/min"
                    :elide-asserts   true
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}

    ]})

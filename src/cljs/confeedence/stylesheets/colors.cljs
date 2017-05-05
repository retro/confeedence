(ns confeedence.stylesheets.colors
  (:require [garden.color :as color]))

(def colors {:green "#25bf71"
             :green-hover "#1c9d4e"
             :green-light "#e4f7ed"
             :green-light-hover "#caefdd"
             :blue "#3577cb"
             :blue-hover "#1c4ea4"
             :blue-light "#e4edf9"
             :blue-light-hover "#ceddf4"
             :red "#e1004c"
             :red-light "#fbdde9"
             :yellow "#ffc832"
             :yellow-light "#fff8e6"
             :purple "#7d35ce"
             :purple-light "#eee3f8"
             :orange "#f57c00"
             :orange-light "#fdefe0"
             :dark "#2d3234"
             :gray "#606569"
             :semi-gray "#74797d"
             :light-gray "#bcc3c9"
             :lighter-gray "#e8ebed"
             :lighter-gray-hover "#d1d7db"
             :lightest-gray "#f3f5f6"
             :white "#ffffff"})

(defn make-color-variations [colors]
  (reduce-kv (fn [m k v]
               (let [base-name (name k)]
                 (assoc m
                        k v
                        (keyword (str base-name "-l")) (color/as-hex (color/lighten v 10))
                        (keyword (str base-name "-d")) (color/as-hex (color/darken v 10))))) {} colors))


(def colors-with-variations (make-color-variations colors))

(defn transition [prop]
  (str (name prop) " 0.10s ease-in-out"))

(defn gen-colors-styles [class-name prop]
  (map (fn [[color-name val]]
         (let [color-name (name color-name)
               normal-class (str "." class-name "-" color-name)
               hover-class (str "." class-name "-h-" color-name)
               darken-val (color/darken val 10)
               lighten-val (color/lighten val 10)
               hover ":hover"
               make-important #(str %1 " !important")]
           [[normal-class {prop val}]
            [(str normal-class "-d") {prop darken-val}]
            [(str normal-class "-l") {prop lighten-val}]
            [(str hover-class hover) {prop val}]
            [(str hover-class "-d" hover) {prop darken-val}]
            [(str hover-class "-l" hover) {prop lighten-val}]
            
            [(str normal-class "-i") {prop (make-important val)}]
            [(str normal-class "-d-i") {prop (make-important darken-val)}]
            [(str normal-class "-l-i") {prop (make-important lighten-val)}]
            [(str hover-class "-i" hover ) {prop (make-important val)}]
            [(str hover-class "-d-i" hover) {prop (make-important darken-val)}]
            [(str hover-class "-l-i" hover) {prop (make-important lighten-val)}]])) colors))

(defn stylesheet [] [[:.bg-transparent {:background 'transparent}]
                     (gen-colors-styles "bg" :background-color)
                     (gen-colors-styles "c" :color)
                     (gen-colors-styles "bd" :border-color)
                     [:.t-c {:transition (transition :color)}]
                     [:.t-bg {:transition (transition :background-color)}]
[:.t-bd {:transition (transition :border-color)}]])

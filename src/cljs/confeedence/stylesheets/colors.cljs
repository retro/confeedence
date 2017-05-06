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

(def theme-colors
  [{:color "#1abc9c" :name "Turquoise" :slug "turquoise"}
   {:color "#2ecc71" :name "Emerald" :slug "emerald"}
   {:color "#3498db" :name "Peter River" :slug "peter-river"}
   {:color "#9b59b6" :name "Amethyst" :slug "amethyst"}
   {:color "#34495e" :name "Wet Asphalt" :slug "wet-asphalt"}
   {:color "#16a085" :name "Green Sea" :slug "green sea"}
   {:color "#27ae60" :name "Nephritis" :slug "nephritis"}
   {:color "#2980b9" :name "Belize Hole" :slug "belize-hole"}
   {:color "#8e44ad" :name "Wisteria" :slug "wisteria"}
   {:color "#2c3e50" :name "Midnight Blue" :slug "midnight-blue"}
   {:color "#f1c40f" :name "Sun Flower" :slug "sun-flower"}
   {:color "#e67e22" :name "Carrot" :slug "carrot"}
   {:color "#e74c3c" :name "Alizarin" :slug "alizarin"}
   {:color "#ecf0f1" :name "Clouds" :slug "clouds"}
   {:color "#95a5a6" :name "Concrete" :slug "concrete"}
   {:color "#f39c12" :name "Orange" :slug "orange"}
   {:color "#d35400" :name "Pumpkin" :slug "pumpkin"}
   {:color "#c0392b" :name "Pomegranate" :slug "pomegranate"}
   {:color "#bdc3c7" :name "Silver" :slug "silver"}
   {:color "#7f8c8d" :name "Asbestos" :slug "asbestos"}
   {:color "#222222" :name "Dark Gray" :slug "dark-gray"}
   {:color "#ffffff" :name "White" :slug "white"}])

(def theme-colors-by-slug
  (reduce (fn [acc c] (assoc acc (:slug c) (:color c))) {} theme-colors))

(defn stylesheet [] [[:.bg-transparent {:background 'transparent}]
                     (gen-colors-styles "bg" :background-color)
                     (gen-colors-styles "c" :color)
                     (gen-colors-styles "bd" :border-color)
                     [:.t-c {:transition (transition :color)}]
                     [:.t-bg {:transition (transition :background-color)}]
                     [:.t-bd {:transition (transition :border-color)}]])

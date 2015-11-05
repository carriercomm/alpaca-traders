(ns alpaca-traders.nav-bar
  (:require [reagent.core :as reagent :refer [atom]]))

(def links [["[Alpaca Traders]" "#"]
            ["Buy" "#"]
            ["Sell" "#post/create"]
            ["About" "#about"]])

(defn to-nav-bar-link [[title link]]
  [:a {:href link :key title}
   [:span
    [:h1 title]]])

(defn create [] 
  [:div.mast
   (map to-nav-bar-link links)])

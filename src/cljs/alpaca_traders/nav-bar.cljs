(ns alpaca-traders.nav-bar
  (:require [reagent.core :as reagent :refer [atom]])
  )

(defn create [] 
  [:nav.navbar.navbar-default
   [:div.navbar-header
    [:a.navbar-brand {
                      :href "#"}
     "Alpaca Traders"
    ]]
   ]
  )

(ns alpaca-traders.nav-bar
  (:require [reagent.core :as reagent :refer [atom]])
  )

(def links [["Postings" "#"]
            ["New Post" "#post/create"]
            ["About" "#about"]
            ])

(defn to-nav-bar-link [[title link]]
  [:a.navbar-brand 
   {:href link
    :key title}
   title]
  )

(defn create [] 
  [:nav.navbar.navbar-default
   [:div.navbar-header
   (do 
     (map to-nav-bar-link links)
     )]
   ]
  )

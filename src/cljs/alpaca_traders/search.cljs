(ns alpaca-traders.search
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch]]
            [alpaca-traders.money-group :as money])
  )

(defn item-to-option [item prefix]
  "Create an HTML Option. If value is nil, option is disabled. 
  Defaults to disabled options."
  (let [{value :id 
         label :name} item]
    [:option {:value value
              :key (str prefix value)
              } label]
    )
  )

(defn item-select [] 
  (let [items (subscribe [:items])
        options  (map #(item-to-option % " item") @items)]
    [:select {:key "item-select"
              :on-change #(dispatch [:select-item 1])
              :value @(subscribe [:item])} options]
    )
  )

(defn server-select [] 
  (let [servers (subscribe [:servers])
        options  (map #(item-to-option % " server") @servers)]
    [:select {:on-change #(dispatch [:select-server 1])
              :value @(subscribe [:server])} options]
    )
  )

(defn create [] 
  [:div.default-body.search
    (item-select)
    (-> @(subscribe [:item]) str)
    (server-select)
    (-> @(subscribe [:server]) str)
   ]
  )
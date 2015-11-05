(ns alpaca-traders.search
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch]]
            [alpaca-traders.money-group :as money]))

(defn item-to-option [item prefix]
  "Create an HTML Option. If value is nil, option is disabled. 
  Defaults to disabled options."
  (let [{value :id 
         label :name} item]
    [:option {:value value
              :key (str prefix value)
              } label]))

(defn item-select [] 
  (let [items (subscribe [:items])
        options  (map #(item-to-option % " item") @items)]
    [:select {:key "item-select"
              :on-change #(dispatch [:select-item (.-target.value %)])
              :value @(subscribe [:item])} options]))

(defn server-select [] 
  (let [servers (subscribe [:servers])
        options  (map #(item-to-option % " server") @servers)]
    [:select {:on-change #(dispatch [:select-server (.-target.value %)])
              :value @(subscribe [:server])} options]))

(defn listings-table [listings]
  [:div])

(defn create [] 
  [:div.default-body.search
   (item-select)
   (-> @(subscribe [:item]))
   (server-select)
   (-> @(subscribe [:server]))
   [:button.btn.btn-default 
      {:type "button"
       :on-click #(dispatch [:search-for-listings 1 2])}
     "Search"]
   (str @(subscribe [:listings]))])
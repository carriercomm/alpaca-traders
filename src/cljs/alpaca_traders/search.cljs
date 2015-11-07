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

(defn table-row [listing] 
  (let [{contact-name :contact-name
         item :item
         price :price 
         quantity :quantity
         server :server} listing
        ppu (if (= 1 quantity) "-" 
              (money/ppu-view listing))]
    [:tr {:key (str contact-name item price)}
     [:td item]
     [:td (money/price-view price)]
     [:td quantity]
     [:td ppu]
     [:td server]
     [:td contact-name]
     ]))

(defn listings-table []
  ;;Obviously a table that has listings in it.
  ;;fixme; Take out item/server depending on applied filters? 
  (let [listings @(subscribe [:listings])
        listings-count @(subscribe [:listings-count])]
    [:div.panel.panel-default
     [:div.panel-heading 
      "Showing results for " listings-count " listing(s)."]
     [:table.table
      [:thead
       [:tr
        [:th "Item"]
        [:th "Price"]
        [:th "Quantity"]
        [:th "Price Per Unit"]
        [:th "Server"]  
        [:th "Contact"]]]
      [:tbody
       (map table-row listings)]]]))

(defn create [] 
  [:div.search
   [:div.default-body
    [:div
     (item-select)
     (server-select)]
    [:button.btn.btn-default 
     {:type "button"
      :on-click #(dispatch [:search-for-listings 1 2])} "Search"]
    ]
   (listings-table)])
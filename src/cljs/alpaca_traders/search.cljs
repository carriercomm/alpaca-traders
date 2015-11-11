(ns alpaca-traders.search
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch]]
            [alpaca-traders.money-group :as money]))

(defn item-to-option [item prefix]
  "Create an HTML Option."
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

(defn listings-filter []
  [:div.default-body.search.filter
   [item-select]
   [server-select]
   [:button.btn.btn-default 
    {:type "button"
     :on-click #(dispatch [:search-for-listings 1 2])} "Search"]
   ])

(defn panel-heading [row-count]
  (let [plural (if (= 1 row-count) "" "s")]
    [:div.panel.panel-heading 
      "Showing " row-count " listing" plural "."]))

(defn table-row [listing] 
  (let [{:keys [item price quantity server]} listing
        quantity (-> listing :quantity str .toLocaleString)
        price (money/price-view price)]
    [:tr {:key (str server item price)}
     [:td item]
     [:td server]
     [:td.numeric quantity]
     [:td.numeric price]]))

(defn listings-table []
  "fixme; Take out item/server depending on applied filters?" 
  (let [listings @(subscribe [:listings])
        listings-count @(subscribe [:listings-count])]
    [:div.search
     [panel-heading listings-count]
     [:table.table
      [:thead
       [:tr
        [:th "Item"]
        [:th "Server"]  
        [:th.numeric "Num"]
        [:th.numeric "At Price"]]]
      [:tbody
       (map table-row listings)]]]))

(defn create [] 
   [:div
    [listings-filter]
    [listings-table]])
(ns alpaca-traders.new-posting
  (:require [reagent.core :as reagent :refer [atom]]
   [alpaca-traders.money-group :as money-group :refer [to-coppers to-money-group rebalance placeholders]]))

(def input-state (reagent/atom {
                                 :price {
                                          :platinum 0
                                          :gold 0
                                          :silver 0
                                          :copper 0
                                          }
                                 :price-per-unit {
                                                   :platinum 0
                                                   :gold 0
                                                   :silver 0
                                                   :copper 0
                                                   }
                                 :quantity 1
                                 }))


(defn currency-input [input-group-type param]
  (print param)
  [:div [:label (param placeholders)]
   [:input {
             :type "number"
             :min "0"
             :placeholder (param placeholders)
             :on-change #(swap! input-state assoc-in [input-group-type param] (int (.-target.value %)))
             :on-blur #(swap! input-state assoc input-group-type (rebalance @input-state))
             :value (get-in @input-state [input-group-type param])
             }]
   ])

(defn input-group [input-group-type]
  [:div (map (partial currency-input input-group-type) (keys placeholders))])

(defn create []
  [:div
   [:h2 "State your price, dingus."]
   [:label "Quantity"]
   [:input {
             :type "number"
             :min "1"
             :on-change #(swap! input-state assoc :quantity (int (.-target.value %)))
             :value (:quantity @input-state)}]

   [input-group :price]

   [:h2 "Price Per Unit"]
   [input-group :price-per-unit]
   [:div (str @input-state)]
   ])
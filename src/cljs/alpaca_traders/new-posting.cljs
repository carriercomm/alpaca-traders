(ns alpaca-traders.new-posting
  (:require [reagent.core :as reagent :refer [atom]]
   [alpaca-traders.money-group :as money-group :refer [to-coppers to-money-group rebalance placeholders]]))

(def default-money-group {
                           :platinum 0
                           :gold 0
                           :silver 0
                           :copper 0
                           }
  )

(def input-state (reagent/atom {:price default-money-group
                                :price-per-unit default-money-group
                                :quantity 1
                                }))


(defn currency-input [input-type param]
  (print input-type param)
  [:div {:key (str input-type param @input-state)}
   [:label (param placeholders)]
   [:input {
             :type "number"
             :min "0"
             :placeholder (param placeholders)
             :on-change #(swap! input-state assoc-in [input-type param] (int (.-target.value %)))
             :on-blur #(swap! input-state assoc input-type (rebalance (input-type @input-state)))
             :value (get-in @input-state [input-type param])
             }]
   ])

(defn input-group [input-group-type]
  [:div
   [currency-input input-group-type :platinum]
   [currency-input input-group-type :gold]
   [currency-input input-group-type :silver]
   [currency-input input-group-type :copper]
   ])

(defn create []
  [:div [:h2 "State your price, dingus."]
   [:label "I want to sell"]
   [:input {
             :type "number"
             :min "1"
             :on-change #(swap! input-state assoc :quantity (int (.-target.value %)))
             :value (:quantity @input-state)}]

   [input-group :price]

   [:h2 "Price Per Unit"]
   [input-group :price-per-unit]
   ])
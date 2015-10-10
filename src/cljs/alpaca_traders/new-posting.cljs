(ns alpaca-traders.new-posting
  (:require [reagent.core :as r :refer [atom]]
            [alpaca-traders.money-group :as money-group :refer [to-coppers to-money-group rebalance placeholders]]
            [cljs.test :refer-macros [deftest is testing run-tests]]))

(defonce default-money-group {
                              :platinum 0
                              :gold 0
                              :silver 0
                              :copper 0
                              }
  )

(def input-state (r/atom {:price default-money-group
                          :quantity 1
                          :pricing "Total"
                          }))


(defn currency-input [param]
  [:div.currency-row 
   [:label.currency-label (param placeholders)]
   [:input.currency {
                     :type "number"
                     :min "0"
                     :placeholder (param placeholders)
                     :on-change #(swap! input-state assoc-in [:price param] (int (.-target.value %)))
                     :on-blur #(swap! input-state assoc :price (rebalance (:price @input-state)))
                     :value (get-in @input-state [:price param])
                     }]
   ])

(defn input-group [on-change]
  [:div 
   [currency-input :platinum]
   [currency-input :gold]
   [currency-input :silver]
   [currency-input :copper]
   ])

(defn resolve-ppu []
  (let [total-price (:price @input-state)
        quantity (:quantity @input-state)]
    (swap! input-state assoc :price-per-unit (to-money-group (/ (to-coppers total-price) quantity)))
    )
  )

(defn resolve-total-price []
  (let [ppu (:price-per-unit @input-state)
        quantity (:quantity @input-state)]
    (swap! input-state assoc :price (to-money-group (* (to-coppers ppu) quantity)))
    )
  )

(defn pricing-input [value]
  "value -> str"
  (let [pricing (:pricing @input-state)
        checked (if (= value pricing) 
                  "checked" 
                  "" )]
    [:input {
             :key (str value checked)
             :type "radio"
             :name "Pricing"
             :value value
             :checked checked
             :on-change #(swap! input-state assoc :pricing (.-target.value %))
             }
     value]
    ))

(defn quantity-input []
  [:input {
           :type "number"
           :min "1"
           :on-change #(swap! input-state assoc :quantity (int (.-target.value %)))
           :value (:quantity @input-state)}] 
  )

(defn create []
  (let [pricing (:pricing @input-state) ]
    
    [:div 
     (if (= pricing "Total")
       [:div [:h2 "Price"]
        [input-group #()]
        ]
       
       [:div
        [:h2 "Price Per Unit"]
        [input-group #()]
        ]
       )
     [:label "I want to sell"]
     [quantity-input]
     [:div (doall (map pricing-input ["Total" "PPU"]))]
     ]
    )   
  )

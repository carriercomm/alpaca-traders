(ns alpaca-traders.new-posting
  (:require [reagent.core :as r :refer [atom]]
            [alpaca-traders.money-group :as money]
            [cljs.test :refer-macros [deftest is testing run-tests]]))

(def input-state (r/atom {:price money/default-group
                          :quantity 1
                          :use-ppu false
                          }))

(defn change-handler [input-state] 
  
  )

(defn currency-input [param]
  (let [placeholder (param money/names)]
    [:div.currency-row {:key (str param)}
     [:label.currency-label placeholder]
     [:input.currency {
                       :type "number"
                       :min "0"
                       :placeholder placeholder 
                       :on-change #(swap! input-state assoc-in [:price param] (int (.-target.value %)))
                       :on-blur #(swap! input-state assoc :price (money/rebalance (:price @input-state)))
                       :value (get-in @input-state [:price param])
                       }]
     [:div {:class (str (name param))}]
     ]
    )
  )


(defn input-group []
  [:div 
   ; Should be le map 
   (doall (map currency-input [:platinum :gold :silver :copper]))
   ])

(defn resolve-ppu! []
  (let [{total-price :price
        quantity :quantity} @input-state]
    (swap! input-state assoc :price-per-unit (money/to-group (/ (money/to-coppers total-price) quantity)))
    )
  )

(defn resolve-total-price! []
  (let [{ppu :price-per-unit
        quantity :quantity} @input-state]
    (swap! input-state assoc :price (money/to-group (* (money/to-coppers ppu) quantity)))
    )
  )

(defn toggle-ppu [value]
  "value -> str"
  (let [ppu? (:use-ppu @input-state)
        name (if (true? ppu?) 
               "Total Price?"
               "Per Unit?")]
    [:a {
         :on-click #(swap! input-state assoc :use-ppu (not ppu?))
         }
     name]
    ))

(defn quantity-input []
  (let [quantity (:quantity @input-state)
        plural (if (= 1 quantity) "" "s")]
    [:div.quantity
     [:input.quantity.off {
                           :type "number"
                           :id "quantity"
                           :min "1"
                           :on-change #(swap! input-state assoc :quantity (int (.-target.value %)))
                           :value (:quantity @input-state) }] 
     [:label {:for "quantity"} "unit" plural]
     ]
    )
  )

(defn create []
  (let [ppu? #(-> @input-state :use-ppu true?)
        quantity (:quantity @input-state)
        title (if (ppu?) "Price Per Unit" "Total Price")
        total-copper (money/to-coppers (:price @input-state))
        summary-display (if (and (not= quantity 1)
                                 (pos? total-copper)) "" "none")]
    
    [:div 
     [:h2.ppu-title  title]
     [toggle-ppu] 
     [:div
      [input-group]
      [quantity-input]
      ]
     
       (if (ppu?)
         [:p {:style {:display summary-display}} 
          "Total ➔ " (-> @input-state money/to-total money/to-string)]
         [:p {:style {:display summary-display}}
          "Cost per unit ➔ "(-> @input-state money/to-ppu money/to-string) ]
         )
     ]
    )
  )

(deftest test-handler-ppu
  (let [start-state (assoc @input-state :use-ppu true)
        end-state (change-handler start-state)]
    )  )


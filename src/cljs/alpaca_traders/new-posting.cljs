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

(deftest test-handler-ppu
  (let [start-state (assoc @input-state :use-ppu true)
        end-state (change-handler start-state)]
    )  )

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
     ]
    )
  )


(defn input-group []
  [:div 
   ; Should be le map 
   (doall (map currency-input [:platinum :gold :silver :copper]))
   ])

(defn resolve-ppu []
  (let [total-price (:price @input-state)
        quantity (:quantity @input-state)]
    (swap! input-state assoc :price-per-unit (money/to-group (/ (money/to-coppers total-price) quantity)))
    )
  )

(defn resolve-total-price []
  (let [ppu (:price-per-unit @input-state)
        quantity (:quantity @input-state)]
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
  [:div.quantity
  [:input.quantity.off {
           :type "number"
           :id "quantity"
           :min "1"
           :on-change #(swap! input-state assoc :quantity (int (.-target.value %)))
           :value (:quantity @input-state) }] 
  [:label {:for "quantity"} "units"]
  ]
  )

(defn create []
  (let [ppu? #(-> @input-state :use-ppu true?)
        title (if (ppu?) "Price Per Unit" "Total Price")]
    
    [:div 
     [:h2.ppu-title  title]
     [toggle-ppu] 
     [:div
      [input-group]
      [quantity-input]
      ]

     (if (ppu?)
       [:p (str (money/to-ppu @input-state)) "PPU"]
       [:p (str (money/to-total @input-state)) "Total"]
       )   
     ]
    )
  )

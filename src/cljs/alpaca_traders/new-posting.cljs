(ns alpaca-traders.new-posting
  (:require [reagent.core :as r :refer [atom]]
            [alpaca-traders.money-group :as money]
            [cljs.test :refer-macros [deftest is testing run-tests]]))

(def input-state (r/atom {:price money/default-group
                          :quantity 1
                          :use-ppu false
                          }))

(defn toggle-ppu [state]
  "value -> str"
  (let [ppu? (:use-ppu @state)
        name (if (true? ppu?) 
               "Total Price?"
               "Per Unit?")]
    [:a {
         :on-click #(swap! state assoc :use-ppu (not ppu?))
         }
     name]
    )
  )

(defn currency-input [state param ppu? quantity]
  (let [placeholder (param money/names)]
    [:div.currency-row {:key (str param)}
     [:label.currency-label placeholder]
     [:input.currency {
                       :type "number"
                       :min "0"
                       :placeholder placeholder 
                       :on-change #(swap! state assoc-in [:price param] (int (.-target.value %)))
                       :on-blur #(swap! state assoc :price (money/rebalance (:price @input-state)))
                       :value (get-in @state [:price param])
                       }]
     [:div {:class (str (name param))}]
     ]
    )
  )

(defn input-group [state ppu? quantity]
  (let [currency-keys [:platinum :gold :silver :copper]]
    [:div 
     (doall (map #(currency-input state % ppu? quantity) currency-keys))
     ]
    )
  )

(defn quantity-input [state]
  (let [quantity (:quantity @state)
        plural (if (= 1 quantity) "" "s")]
    [:div.quantity
     [:input.quantity.off {
                           :type "number"
                           :id "quantity"
                           :min "1"
                           :on-change #(swap! state assoc :quantity (int (.-target.value %)))
                           :value (:quantity @input-state) }] 
     [:label {:for "quantity"} "unit" plural]
     ]
    )
  )

(defn create []
  (let [state input-state
        ppu? #(-> @state :use-ppu true?)
        quantity (:quantity @state)
        title (if (ppu?) "Price Per Unit" "Total Price")
        total-copper (money/to-coppers (:price @state))
        summary-display (if (and (> quantity 1)
                                 (pos? total-copper)) "" "none")]
    
    [:div 
     [:h2.ppu-title title]
     [toggle-ppu state] 
     [:div
      [input-group state]
      [quantity-input state]
      ]
     
     (if (ppu?)
       [:p {:style {:display summary-display}} 
        "Total ➔ " (-> @state money/to-total money/to-string)]
       [:p {:style {:display summary-display}}
        "Cost per unit ➔ "(-> @state money/to-ppu money/to-string) ]
       )
     ]
    )
  )

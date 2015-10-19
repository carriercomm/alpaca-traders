(ns alpaca-traders.new-posting
  "Reagent component to create a new posting on alpaca-traders"
  (:require [reagent.core :as r :refer [atom]]
            [alpaca-traders.money-group :as money]
            [cljs.test :refer-macros [deftest is testing run-tests]]
            [ajax.core :refer [POST]])
             )

(def test-items [{:name "Choose an item" :id nil}
                 {:name "Bone chips" :id 1}
                 {:name "Duck chips" :id 2}
                 ]
  )

(def test-servers [{:name "Choose a server" :id nil}
                   {:name "Mal'Ganis" :id 1}
                   {:name "Tyrael" :id 2}
                   ]
  )

(defn item-to-option [item prefix]
  "Create an HTML Option. If value is nil, option is disabled. 
  Defaults to disabled options."
  (let [{value :id 
         label :name} item]
    [:option {:value value
              :key (str prefix value)
              :disabled (nil? value)
              } label]
    )
  )

(defn item-select [state items] 
  (let [options  (map #(item-to-option % " item") items)]
    [:select {:key "item-select"
               :on-change #(swap! state assoc :item-id (int (.-target.value %)))
               :value (:item-id @state)
              }
     options
     ]
    )
  )

(defn server-select [state servers] 
  (let [options  (map #(item-to-option % " server") servers)]
    [:select {:key "server-select"
               :on-change #(swap! state assoc :server-id (int (.-target.value %)))
               :value (:server-id @state)
              }
     options
     ]
    )
  )

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

(defn currency-value [state param ppu?]
  (let [{currency :price
         quantity :quantity} @state]
    (if (true? ppu?)
      (-> currency param (/ quantity))
      (-> currency param)
      )
    )
  )

(defn currency-input [state param ppu? quantity]
  (let [currency (str (name param))
        value (currency-value state param ppu?)]
    [:div.currency-row {:key (str param)}
     [:input.currency {
                       :id currency
                       :type "number"
                       :min "0"
                       :on-change #(swap! state assoc-in [:price param] (int (.-target.value %)))
                       :on-blur #(swap! state assoc :price (money/rebalance (:price @state)))
                       :value value
                       }]
     [:label.currency {
              :for currency
              :class currency
              :title currency}]
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
                           :value (:quantity @state) }] 
     [:label {:for "quantity"} "unit" plural]
     ]
    )
  )

(defn submit [state] 
  (let [request {:method :post
                 :json @state
                 :response-format :json
                 }
        response (POST "/new-posting" request)]
    (.log js/console response)
  )
)

(defn valid? [state]
  "Server and item must not be nil. 
  Might want to check if they're natural numbers as well."
  (let [{item :item-id
         server :server-id} @state]
    (and item server)
    )
  )

(defn submit-button [state]
  [:button.btn.btn-default {:type "button"
                            :on-click (partial submit state)
                            :disabled (not (valid? state))}
   "Ready to submit?"]
  )


(def default-state (r/atom {:price money/default-group
                          :quantity 1
                          :use-ppu false
                          :item-id nil
                          :server-id nil
                          }))

(defn create []
  (let [state default-state 
        ppu? #(-> @state :use-ppu true?)
        quantity (:quantity @state)
        title (if (ppu?) "Price Per Unit" "Total Price")
        total-copper (money/to-coppers (:price @state))
        summary-display (if (and (> quantity 1)
                                 (pos? total-copper)) "" "none")
        items test-items
        servers test-servers]
    
    [:div 
     [:h1.ppu-title title]
     [toggle-ppu state] 
     [:div
      [item-select state items]
      [server-select state test-servers]
      [input-group state]
      [quantity-input state]
      [submit-button state]
      ]
     
     (if (ppu?)
       [:p {:style {:display summary-display}} 
        "Total ➔ " (-> @state money/to-total money/to-string) " for " quantity " units."]
       [:p {:style {:display summary-display}}
        "Cost per unit ➔ "(-> @state money/to-ppu money/to-string) ]
       )
     ]
    )
  )

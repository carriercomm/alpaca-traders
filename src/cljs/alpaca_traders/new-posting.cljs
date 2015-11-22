(ns alpaca-traders.new-posting
  "Reagent component to create a new posting on alpaca-traders"
  (:require [clojure.string :refer [capitalize]]
            [reagent.cookies :as cookie]
            [reagent.core :as r :refer [atom]]
            [alpaca-traders.money-group :as money]
            [cljs.test :refer-macros [deftest is testing run-tests]]
            [ajax.core :as ajax]))

(def test-items [{:name "Choose an item" :id nil}
                 {:name "Bone chips" :id 1}
                 {:name "Duck chips" :id 2}])

(def test-servers [{:name "Choose a server" :id nil}
                   {:name "Mal'Ganis" :id 1}
                   {:name "Tyrael" :id 2}])

(defn item-to-option [item prefix]
  "Create an HTML Option. If value is nil, option is disabled. 
  Defaults to disabled options."
  (let [{value :id 
         label :name} item]
    [:option {:value value
              :key (str prefix value)
              } label]))

(defn item-select [state items] 
  (let [options  (map #(item-to-option % " item") items)]
    [:select {:key "item-select"
               :on-change #(swap! state assoc :item-id (int (.-target.value %)))
               :value (:item-id @state)
              }
     options]))

(defn server-select [state servers] 
  (let [options  (map #(item-to-option % " server") servers)]
    [:select {:key "server-select"
               :on-change #(swap! state assoc :server-id (int (.-target.value %)))
               :value (:server-id @state)
              }
     options]))

(defn toggle-ppu [state]
  "value -> str"
  (let [ppu? (:use-ppu @state)
        name (if (true? ppu?) 
               "Total Price?"
               "Per Unit?")]
    [:a {
         :on-click #(swap! state assoc :use-ppu (not ppu?))
    }
     name]))

(defn currency-value [state param ppu?]
  (let [{currency :price
         quantity :quantity} @state]
    (if (true? ppu?)
      (-> currency param (/ quantity))
      (-> currency param))))

(defn currency-input [state param ppu? quantity]
  (let [currency (str (name param))
        value (currency-value state param ppu?)]
    [:div.currency-input {:key (str param)}
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
              :title (capitalize currency)}]]))

(defn input-group [state ppu? quantity]
  (let [currency-keys [:platinum :gold :silver :copper]]
    [:div
     (doall (map #(currency-input state % ppu? quantity) currency-keys))
     ]))

(defn quantity-input [state]
  (let [quantity (:quantity @state)
        plural (if (= 1 quantity) "" "s")]
    [:div.quantity
     [:input {:type "number"
              :id "quantity"
              :min "1"
              :on-change #(swap! state assoc :quantity (int (.-target.value %)))
              :value (:quantity @state) }] 
     [:label {:for "quantity"} "unit" plural]
     ]))

(defn submit [state] 
  (let [csrf (cookie/get :ring-session)
        request {
                 :uri "/new-posting"
                 :method :post
                 :handler #(-> % js/console.log)
                 :params (clj->js @state)
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format)
                 }
        response (ajax/ajax-request request)]
    ))

(defn valid? [state]
  "Server and item must not be nil. 
  Might want to check if they're natural numbers as well."
  (let [{item :item-id
         server :server-id} @state]
    ;(and item server)
    true))

(defn submit-button [state]
  [:button.btn.btn-default {:type "button"
                            :on-click (partial submit state)
                            :disabled (not (valid? state))}
   "Ready to submit?"])


(def default-state (r/atom {:price 
                            (assoc money/default-group :platinum 4)
                          :quantity 3 
                          :use-ppu false
                          :item-id nil
                          :server-id nil
                          }))

(defn create []
  (let [state default-state 
        ppu? #(-> @state :use-ppu true?)
        quantity (:quantity @state)
        title (if (ppu?) "Price Per Unit" "Total Price")
        total-copper (-> @state :price money/to-coppers)
        summary-display (if (and (> quantity 1)
                                 (pos? total-copper)) "" "none")
        items test-items
        servers test-servers]
    
    [:div.default-body.new-posting 
     [:h1.ppu-title title]
     [toggle-ppu state] 
     [:div
      [server-select state test-servers]
      [item-select state items]
      [input-group state]
      [quantity-input state]
      
      [:div.alert.alert-info.summary 
       {:style {:display summary-display}} 
       (if (ppu?) 
         [:div "Total ➔ " [money/total-view @state]]
         [:div "Cost per unit ➔ " [money/ppu-view @state]])]
      ]
     [submit-button state]
     ]))

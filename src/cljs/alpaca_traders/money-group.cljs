(ns alpaca-traders.money-group
  (:require [reagent.core :as reagent :refer [atom]]))

(def input-state (reagent/atom {
                                 :price {
                                          :platinum 4
                                          :gold 3
                                          :silver 2
                                          :copper 1
                                          }
                                 :quantity 1
                                 }))

(def currency-to-copper {
                          :platinum 10000
                          :gold 1000
                          :silver 100
                          :copper 1
                          })

(defn currency-input [param placeholder]
  [:input {
            :type "number"
            :min "0"
            :placeholder placeholder
            :on-change #(swap! input-state
                          assoc-in [:price param] (int (.-target.value %)))
            :value (get-in @input-state [:price param])
            }]
  )

(defn create []
  [:div [:input {
                  :id "Bronze"
                  :type "text"
                  :placeholder "Price Per Unit"
                  :value (get-in @input-state [:price :platinum])}]
   [currency-input :platinum "Platinum"]
   [currency-input :gold "Gold"]
   [currency-input :silver "Silver"]
   [currency-input :copper "Copper"]
   ])


(defn reduce-to-money-group [m]
  (let [keys-left (:keys-left m)
        current-key (first keys-left)
        copper-left (:amount-left m)
        conversion-rate (get currency-to-copper current-key)]
    (if (= current-key nil)
      (dissoc m :keys-left :amount-left)
      (reduce-to-money-group
        (assoc m current-key (int (/ copper-left conversion-rate))
                 :keys-left (rest keys-left)
                 :amount-left (rem copper-left conversion-rate))))))


(defn to-money-group [copper-amount]
  (let [k-order [:platinum :gold :silver :copper]]
    (reduce-to-money-group {:amount-left copper-amount, :keys-left k-order})))
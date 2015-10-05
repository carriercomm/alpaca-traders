(ns alpaca-traders.new-posting
  (:require [reagent.core :as reagent :refer [atom]]))

(def input-state (reagent/atom {
                                 :price {
                                          :platinum 0
                                          :gold 0
                                          :silver 0
                                          :copper 0}
                                 :quantity 1
                                 }))

(defn create []
  [:div [:input {:type "text"
                 :placeholder "Price Per Unit"}]
   [:input {:type "text"
            :placeholder "Platinum"
            :value (get-in @input-state [:price :platinum])}]
   [:input {:type "text"
            :placeholder "Gold"
            :value (get-in @input-state [:price :gold])}]
   [:input {:type "text"
            :placeholder "Silver"}]
   [:input {:type "text"
            :placeholder "Copper"}]
   ])
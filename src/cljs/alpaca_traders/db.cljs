(ns alpaca-traders.db
  (:require 
    [alpaca-traders.money-group :as money]))

(def test-items [{:name "Choose an item" :id nil}
                 {:name "Bone chips" :id 1}
                 {:name "Duck chips" :id 2}])

(def test-servers [{:name "Choose a server" :id nil}
                   {:name "Mal'Ganis" :id 1}
                   {:name "Tyrael" :id 2}])

(def test-listings [{:item "Bone chips 2"
                     :contact-name "Nordic Sean" 
                     :server "Mal'Ganis"
                     :price {:platinum 1000
                             :gold 0
                             :silver 0
                             :copper 0}
                     :quantity 100}
                    {:item "Bone chips"
                     :contact-name "Nordic Sean" 
                     :server "Mal'Ganis"
                     :price {:platinum 53
                             :gold 0
                             :silver 0
                             :copper 0}
                     :quantity 20}
                    {:item "I LIKE TO OVERFLOWI LIKE TO OVERFLOWI LIKE TO OVERFLOWI LIKE TO OVERFLOW"
                     :contact-name "Nordic Sean" 
                     :server "Tyrael"
                     :price {:platinum 1000000
                             :gold 1
                             :silver 2
                             :copper 3} 
                     :quantity 1}])

(defn get-listings [item server] 
  test-listings)

(def initial-state 
  {:items test-items 
   :listings (sorted-map) ;;todo sort by field
   :selected-item nil 
   :servers test-servers
   :selected-server nil}
  )
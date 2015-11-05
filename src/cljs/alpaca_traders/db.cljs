(ns alpaca-traders.db
  (:require 
    [alpaca-traders.money-group :as money]))

(def test-items [{:name "Choose an item" :id nil}
                 {:name "Bone chips" :id 1}
                 {:name "Duck chips" :id 2}])

(def test-servers [{:name "Choose a server" :id nil}
                   {:name "Mal'Ganis" :id 1}
                   {:name "Tyrael" :id 2}])

(def test-listings [{:contact-name "Nordic Sean" 
                     :price money/default-group
                     :quantity 2000}])

(defn get-listings [item server] 
  test-listings)

(def initial-state 
  {:items test-items 
   :listings (sorted-map) ;;todo sort by field
   :selected-item nil 
   :servers test-servers
   :selected-server nil}
  )
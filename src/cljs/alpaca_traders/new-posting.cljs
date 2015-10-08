(ns alpaca-traders.new-posting
  (:require [reagent.core :as reagent :refer [atom]]
             [alpaca-traders.money-group :as money-group]))



(defn create []
  [:div
   [money-group/create]
    ])
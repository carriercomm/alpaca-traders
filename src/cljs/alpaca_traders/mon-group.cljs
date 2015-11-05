(ns alpaca-traders.money-group
  (:require [clojure.string :as string] 
            [cljs.test :refer-macros [deftest is testing run-tests]]))

(def default-group {:platinum 0 
                    :gold 0
                    :silver 0
                    :copper 0})

(def currency-to-copper {:platinum 10000
                         :gold 1000
                         :silver 100
                         :copper 1})

(defn money-amount-to-coppz [money-entry]
  (let [[currency amount] money-entry]
    (* (currency currency-to-copper) amount)))

(defn to-coppers [money-group]
  (reduce + (map money-amount-to-coppz money-group)))

(defn- reduce-to-group [m]
  (let [[current-key & keys-left] (:keys-left m)
        copper-left (:amount-left m)
        conversion-rate (get currency-to-copper current-key)]
    (if (nil? current-key)
      (dissoc m :keys-left :amount-left)
      (reduce-to-group
        (assoc m current-key (int (/ copper-left conversion-rate))
          :keys-left keys-left
          :amount-left (rem copper-left conversion-rate))))))

(defn to-group [copper-amount]
  (let [k-order [:platinum :gold :silver :copper]]
    (reduce-to-group {:amount-left copper-amount 
                      :keys-left k-order})))

(defn to-total [ppu-with-quantity]
  (let [{ppu :price 
         quantity :quantity} ppu-with-quantity]
    (-> ppu to-coppers (* quantity) to-group)))

(defn to-ppu [price-with-quantity]
  (let [{price :price
         quantity :quantity} price-with-quantity]
    (-> price to-coppers (/ quantity) to-group)))

(defn value? [currency]
  (let [[_ v] currency]
    (pos? v)))

(defn currency-pair-string [currency] 
  (let [[k v] currency
        key-name (name k)
        amount (.toLocaleString v)]
    (str amount " " key-name)))

(defn to-string [m]
  (let [with-value (filter value? m)
        values (map currency-pair-string with-value)]
    (string/join ", " values)))

(defn rebalance [unbalanced-group]
  (-> unbalanced-group to-coppers to-group))

;; Dumb alpaca views
(defn currency-view [currency-seq] 
  (let [[type value] currency-seq
        should-render? (pos? value)]
    (if should-render?
      [:span {:key currency-seq} 
       [:span.currency-value value]
       [:div.read-only {:class type}]
       ])))

(defn ppu-view [$-with-quantity] 
  (let [ppu (to-ppu $-with-quantity)
        not-free? (-> $-with-quantity :price to-coppers pos?)]
    (if not-free?
      [:div.currency-row (map currency-view ppu)])))


(defn total-view [ppu-with-quantity] 
  (let [total (to-total ppu-with-quantity)
        not-free? (-> total to-coppers pos?)]
    (if not-free?
      [:div.currency-row (map currency-view total)])))

;;
;; Le tests
;;
(deftest test-to-total 
  (let [money-group (assoc default-group :gold 4)
        quantity 2
        m {:price money-group :quantity quantity}
        total (to-total m)
        ]
    (is (= (:gold total) 8))))

(deftest test-to-ppu-rounds
  (let [money-group (assoc default-group :copper 3)
        quantity 2
        state {:price money-group :quantity quantity}
        ppu (to-ppu state)
        ]
    (is (= (:copper ppu) 1))))

(deftest test-to-ppu 
  (let [money-group (assoc default-group :gold 4)
        quantity 2
        state {:price money-group :quantity quantity}
        ppu (to-ppu state)
        ]
    (is (= (:gold ppu) 2))))

(run-tests)

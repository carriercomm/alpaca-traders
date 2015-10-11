(ns alpaca-traders.money-group
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]))

(defonce names {
                       :platinum "Platinum"
                       :gold "Gold"
                       :silver "Silver"
                       :copper "Copper"
                       })

(def default-group {
                        :platinum 0
                        :gold 2
                        :silver 1
                        :copper 0
                        }
  )

(def currency-to-copper {
                         :platinum 10000
                         :gold 1000
                         :silver 100
                         :copper 1
                         })

(defn money-amount-to-coppz [money-entry]
  (let [currency (first money-entry)
        amount (second money-entry)]
    (* (currency currency-to-copper) amount))
  )

(defn to-coppers [money-group]
  (reduce + (map money-amount-to-coppz (seq money-group)))
  )

(defn- reduce-to-group [m]
  (let [keys-left (:keys-left m)
        current-key (first keys-left)
        copper-left (:amount-left m)
        conversion-rate (get currency-to-copper current-key)]
    (if (= current-key nil)
      (dissoc m :keys-left :amount-left)
      (reduce-to-group
        (assoc m current-key (int (/ copper-left conversion-rate))
          :keys-left (rest keys-left)
          :amount-left (rem copper-left conversion-rate))))))

(defn to-group [copper-amount]
  (let [k-order [:platinum :gold :silver :copper]]
    (reduce-to-group {:amount-left copper-amount, :keys-left k-order}))
  )

(defn to-total [ppu-with-quantity]
  (let [ppu (:price ppu-with-quantity)
        quantity (:quantity ppu-with-quantity)]
    (-> ppu to-coppers (* quantity) to-group)
    )                   
  )

(defn to-ppu [price-with-quantity]
  (let [price (:price price-with-quantity)
        quantity (:quantity price-with-quantity)]
    (-> price to-coppers (/ quantity) to-group)
    )   
  )

(defn rebalance [unbalanced-group]
  (-> unbalanced-group to-coppers to-group)
  )

;; Le tests
(deftest test-to-total 
  (let [money-group (assoc default-group :gold 4)
        quantity 2
        m {:price money-group :quantity quantity}
        total (to-total m)
        ]
    (is (= (:gold total) 8)) )
  )

(deftest test-to-ppu-rounds
  (let [money-group (assoc default-group :copper 3)
        quantity 2
        state {:price money-group :quantity quantity}
        ppu (to-ppu state)
        ]
    (is (= (:copper ppu) 1))
    )
  )

(deftest test-to-ppu 
  (let [money-group (assoc default-group :gold 4)
        quantity 2
        state {:price money-group :quantity quantity}
        ppu (to-ppu state)
        ]
    (is (= (:gold ppu) 2))
    )
  )

(run-tests)

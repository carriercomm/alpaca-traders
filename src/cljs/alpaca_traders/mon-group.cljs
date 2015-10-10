(ns alpaca-traders.money-group
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]))

(defonce placeholders {
                       :platinum "Platinum"
                       :gold "Gold"
                       :silver "Silver"
                       :copper "Copper"
                       })

(defonce default-group {
                        :platinum 0
                        :gold 0
                        :silver 0
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

(defn calculate-total [ppu quantity]
  (-> ppu to-coppers (* quantity) to-group)
  )                   

(defn calculate-ppu [total-price quantity]
  (-> total-price to-coppers (/ quantity) to-group)
  )   

(defn rebalance [unbalanced-group]
  (-> unbalanced-group to-coppers to-group)
  )

;; Le tests
(deftest test-calculate-total
  (let [money-group (assoc default-group :gold 4)
        quantity 2
        total (calculate-total money-group quantity)
        ]
    (is (= (:gold total) 8)) )
  )

(deftest test-calculate-ppu-rounds
  (let [money-group (assoc default-group :copper 3)
        quantity 2
        ppu (calculate-ppu money-group quantity)
        ]
    (is (= (:copper ppu) 1))
    )
  )

(deftest test-calculate-ppu 
  (let [money-group (assoc default-group :gold 4)
        quantity 2
        ppu (calculate-ppu money-group quantity)
        ]
    (is (= (:gold ppu) 2))
    )
  )

(run-tests)

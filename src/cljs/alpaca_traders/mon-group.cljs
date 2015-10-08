(ns alpaca-traders.money-group)

(def placeholders {
                    :platinum "Platinum"
                     :gold "Gold"
                     :silver "Silver"
                     :copper "Copper"
                    })
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

(defn rebalance [unbalanced-group]
  (-> unbalanced-group to-coppers to-money-group)
  )
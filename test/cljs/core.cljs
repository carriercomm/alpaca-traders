(ns alpaca-trades.test.core
  :require [cljs.test :refer-macros [deftest is testing run-tests]])

(deftest test-to-total 
  (let [money-group (assoc default-group :gold 4)
        quantity 2
        m {:price money-group :quantity quantity}
        total (to-total m)]
    (is (= (:gold total) 8))))

(deftest test-to-ppu-rounds
  (let [money-group (assoc default-group :copper 3)
        quantity 2
        state {:price money-group :quantity quantity}
        ppu (to-ppu state)]
    (is (= (:copper ppu) 1))))

(deftest test-to-ppu 
  (let [money-group (assoc default-group :gold 4)
        quantity 2
        state {:price money-group :quantity quantity}
        ppu (to-ppu state)]
    (is (= (:gold ppu) 2))))

(run-tests)
(ns colifi.test.report
  (:use [colifi.report])
  (:use [lazytest.describe]))

(def testdata
  [{:date 1 :source #{"Income" "Paycheck"} :target #{"Assets" "Bank" "Checking"} :amount 1500M}
   {:date 2 :source #{"Assets" "Bank" "Checking"} :target #{"Assets" "Cash"} :amount 100M}
   {:date 2 :source #{"Assets" "Cash"} :target #{"Expenses" "Food" "Coffee" "Starbucks"} :amount 4.50M}])

(describe select-tags
          (it "works for a single tag"
              (= (select-tags #{"Cash"} testdata)
                 [{:date 2 :source #{"Assets" "Bank" "Checking"} :target #{"Assets" "Cash"} :amount 100M}
                  {:date 2 :source #{"Expenses" "Food" "Coffee" "Starbucks"} :target #{"Assets" "Cash"} :amount -4.50M}]))
          (it "works for two tags"
              (= (select-tags #{"Income" "Paycheck"} testdata)
                 [{:date 1 :source #{"Assets" "Bank" "Checking"} :target #{"Income" "Paycheck"} :amount -1500M}]))
          (it "ignores intra-tag transactions"
              (= (select-tags #{"Assets"} testdata)
                 [{:date 1 :source #{"Income" "Paycheck"} :target #{"Assets" "Bank" "Checking"} :amount 1500M}
                  {:date 2 :source #{"Expenses" "Food" "Coffee" "Starbucks"} :target #{"Assets" "Cash"} :amount -4.50M}])))

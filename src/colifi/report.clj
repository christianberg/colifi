(ns colifi.report
  (:use [clojure.set :only [subset?]]))

(defn- flip [transaction]
  (merge transaction
         {:source (:target transaction)
          :target (:source transaction)
          :amount (* -1 (:amount transaction))}))

(defn select-tags
  "Select all transactions for a set of tags.

  Transactions will not be included if they are ineffective for the
  tag set, i.e. when the full tag set is included in both the :source
  and the :target sets. The resulting transactions will always have
  the tag set in the :target set, they are flipped if neccessary."
  [tags transactions]
  (keep (fn [t]
          (cond
           (and (subset? tags (:target t)) (not (subset? tags (:source t)))) t
           (and (not (subset? tags (:target t))) (subset? tags (:source t))) (flip t)))
        transactions))

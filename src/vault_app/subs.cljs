(ns vault-app.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :current-page
 (fn [db _]
   (let [page-index (get db :current-page)]
     (get-in db [:page-names page-index]))))

(reg-sub
  :get
  (fn [db [_ option-key]]
    (get db option-key)))

(reg-sub
 :get-options
 (fn [db _]
   (get db :vault-parameters)))

(reg-sub
 :character-restrictions
 (fn [db [_ option-key]]
   (get-in db [:character-restrictions option-key])))

(reg-sub
 :parameter
 (fn [db [_ option-key]]
   (get-in db [:vault-parameters option-key])))

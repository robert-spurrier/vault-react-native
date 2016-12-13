(ns vault-app.handlers
  (:require
    [re-frame.core :refer [reg-event-db reg-event-fx dispatch path]]
    [vault-app.db :refer [app-db]]))


(reg-event-db
 :initialize-db
  (fn [_ _]
    app-db))

(reg-event-db
 :page-change
 (fn [db [_ key]]
   (assoc db :current-page key)))

(reg-event-db
 :set-vault-parameter
 (fn [db [_ key value]]
   (assoc-in db [:vault-parameters key] value)))

(reg-event-db
 :enable-restriction
 (fn [db [_ key]]
   (assoc-in db [:vault-parameters key] 0)))

(reg-event-db
 :disable-restriction
 (fn [db [_ key]]
   (update-in db [:vault-parameters] dissoc key)))

(reg-event-db
 :reset-restrictions
 (fn [db [_]]
   (let [restriction-keys (keys (:character-options db))]
     (update-in db [:vault-parameters] (fn [x] (apply dissoc x restriction-keys))))))

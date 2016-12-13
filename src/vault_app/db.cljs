(ns vault-app.db)

;; initial state of app-db
(def app-db {:current-page 0
             :page-names ["GetVau.lt" "Restrict Characters" "Adjust Restrictions"]
             :max-password-length 100
             :character-options (array-map
                                 :lower "a-z"
                                 :upper "A-Z"
                                 :number "0-9"
                                 :space "space"
                                 :symbol "!@#$%"
                                 :dash "-/_")
             :vault-parameters {:service-name ""
                                :phrase ""
                                :length 20
                                :repeat 0}})

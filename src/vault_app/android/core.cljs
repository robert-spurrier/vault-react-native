(ns vault-app.android.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [vault-app.style :refer [style]]
            [vault-app.handlers]
            [vault-app.subs]))

(def Vault (js/require "vault"))
(def ReactNative (js/require "react-native"))
(def Swiper (js/require "react-native-swiper"))

(def swiper (r/adapt-react-class Swiper))
(def app-registry (.-AppRegistry ReactNative))
(def clipboard (.-Clipboard ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def text-input (r/adapt-react-class (.-TextInput ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def slider (r/adapt-react-class (.-Slider ReactNative)))
(def switch (r/adapt-react-class (.-Switch ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))

(defn dash-switch [{:keys [parameter switch-name]}]
  (let [option-value (subscribe [:parameter parameter])
        symbol-value (subscribe [:parameter :symbol])]
    (fn [props]
      [view {:key (str (name parameter) "-switch")}
       [text (:switch-text style) switch-name]
       [switch (merge props (:switch style)
                      {:disabled (if (= 0 @symbol-value)
                                            (do (dispatch [:disable-restriction parameter])
                                                true)
                                            false)
                                :value (not (nil? @option-value))
                                :on-value-change (fn [value]
                                                   (if value
                                                     (dispatch [:enable-restriction parameter])
                                                     (dispatch [:disable-restriction parameter])))})]])))

(defn character-switch [{:keys [parameter switch-name]}]
  (let [option-value (subscribe [:parameter parameter])]
    (fn [props]
      [view {:key (str (name parameter) "-switch")}
       [text (:switch-text style) switch-name]
       [switch (merge props (:switch style)
                      {:disabled false
                       :value (not (nil? @option-value))
                       :on-value-change (fn [value]
                                          (if value
                                            (dispatch [:enable-restriction parameter])
                                            (dispatch [:disable-restriction parameter])))})]])))

(defn numbered-slider [{:keys [parameter slider-name minimum-value maximum-value on-sliding-complete]}]
  (let [slider-value (subscribe [:parameter parameter])]
    (fn [props]
      [view (merge (:slider-view style)
                   {:key (str (name parameter) "-slider")})
       [view (:slider-text-view style)
        [text (:slider-text style) slider-name]
        [text (:slider-text style) @slider-value]]
       [slider (merge props (:slider style)
                      {:disabled (nil? @slider-value)
                       :value @slider-value
                       :step 1})]])))

(defn password-enable-characters-view [character-options]
 (fn []
     (into
      [view (:switches-view style)]
      (conj (mapv (fn [[parameter switch-name] kv]
                    [character-switch {:parameter parameter
                                       :switch-name switch-name}])
                  (drop-last character-options))
            [dash-switch {:parameter :dash :switch-name "-/_"}]))))

(defn password-restrict-characters-view [character-options]
  (let [maximum-value (subscribe [:parameter :length])]
    (fn []
      (into
       [view (:sliders-view style)]
       (mapv (fn [[parameter slider-name] kv]
               [numbered-slider {:parameter parameter
                                 :slider-name slider-name
                                 :minimum-value 0
                                 :maximum-value @maximum-value
                                 :on-value-change (fn [value]
                                                        (dispatch [:set-vault-parameter parameter value]))}])
             character-options)))))

(defn password-main-view []
  (let [max-password-length (subscribe [:get :max-password-length])]
    (fn []
      [view (:text-entry-view style)
       [view (:service-name-entry-view style)
        [text (:service-name-text style) "Service Name"]
        [text-input (merge (:service-name-text-input style)
                           {:auto-capitalize "none"
                            :underline-color-android "transparent"
                            :on-change-text (fn [text]
                                              (dispatch [:set-vault-parameter :service-name text]))})]]
       [view (:phrase-entry-view style)
        [text (:service-name-text style) "Secret Phrase"]
        [text-input (merge (:phrase-text-input style)
                           {:auto-capitalize "none"
                            :underline-color-android "transparent"
                            :on-change-text (fn [text]
                                              (dispatch [:set-vault-parameter :phrase text]))})]]
       [numbered-slider {:parameter :length
                         :slider-name "Length"
                         :minimum-value 1
                         :maximum-value @max-password-length
                         :on-value-change (fn [value]
                                            (do
                                              (dispatch [:reset-restrictions])
                                              (dispatch [:set-vault-parameter :length value])))}]
       [numbered-slider {:parameter :repeat
                         :slider-name "Repeats"
                         :minimum-value 0
                         :maximum-value @max-password-length
                         :on-value-change (fn [value]
                                            (dispatch [:set-vault-parameter :repeat value]))}]])))

(defn password-button []
  (let [service-name (subscribe [:parameter :service-name])
        phrase (subscribe [:parameter :phrase])
        length (subscribe [:parameter :length])
        repeat (subscribe [:parameter :repeat])
        lower (subscribe [:parameter :lower])
        upper (subscribe [:parameter :upper])
        number (subscribe [:parameter :number])
        dash (subscribe [:parameter :dash])
        space (subscribe [:parameter :space])
        symbol (subscribe [:parameter :symbol])]
    (fn []
      (let [password
            (cond
              (= "" @service-name) "Enter Service Name"
              (= "" @phrase) "Enter Secret Phrase"
              (every? #(not (nil? %)) [@lower @upper @number @space @symbol]) "Disable a Restriction"
              :else (.generate (Vault. (clj->js {:phrase @phrase
                                                 :length @length
                                                 :repeat @repeat
                                                 :lower @lower
                                                 :upper @upper
                                                 :number @number
                                                 :dash @dash
                                                 :space @space
                                                 :symbol @symbol})) @service-name))]
        [touchable-highlight (merge (:password-button style)
                                    {:on-press #(do (.setString clipboard password))})
         [text (:password-text style) password]]))))

(defn page-name []
  (let [page-name (subscribe [:current-page])]
    (fn []
          [text (:page-name-text style) @page-name])))

(defn app-root []
  (let [character-options (subscribe [:get :character-options])]
    (fn [] [view (:app-root-view style)
            [page-name]
            [password-button]
            [swiper (merge (:swiper style)
                           {:on-momentum-scroll-end (fn [_ state _]
                                                      (dispatch [:page-change (.-index state)]))})
             [password-main-view]
             [(password-enable-characters-view @character-options)]
             [(password-restrict-characters-view @character-options)]]])))

(defn init []
      (dispatch-sync [:initialize-db])
      (.registerComponent app-registry "VaultApp" #(r/reactify-component app-root)))

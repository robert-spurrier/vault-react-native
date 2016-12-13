(ns vault-app.style)

(def param-text-input {:background-color "#dcdcdc"
                       :margin-top 5
                       :height 40})

(def param-text {:color "#392D3F"
                 :font-size 16
                 :font-weight "400"})

(def param-layout {:flex-direction "column"
                   :margin-bottom 180
                   :flex 1
                   :justify-content "space-around"
                   :align-items "center"})

(def param-view {:padding-top 20
                 :padding-bottom 20
                 :align-items "stretch"})

(def style
  {:switches-view param-layout
   :switch-text {:style (assoc param-text :text-align "center")}
   :slider-text {:style param-text}
   :slider-text-view {:flex-direction "row"
                      :justify-content "space-between"}
   :switch {:style {:margin-top 5}
            :on-tint-color "#654F6F"
            :tint-color "#dcdcdc"}
   :slider-view {:style param-view}
   :slider {:style {:margin-top 5}
            :minimum-track-tint-color "#654F6F"
            :maximum-track-tint-color "#dcdcdc"}
   :sliders-view (merge
                  param-layout
                  {:margin-left 30
                   :margin-right 30
                   :justify-content "space-around"
                   :align-items "stretch"})
   :text-entry-view {:content-container-style {:align-self "center"
                                               :align-items "center"}
                     :style {:margin 30}}
   :service-name-view {:style {:align-items "stretch"}}
   :service-name-text {:style {:color "#392D3F"
                               :font-size 16
                               :font-weight "500"}}
   :service-name-text-input {
                             :style param-text-input}
   :phrase-entry-view {:style param-view}
   :phrase-text-input {:secure-text-entry true
                       :style param-text-input}
   :password-button {:style {:overflow "hidden"
                             :margin 10
                             :height 70
                             :align-self "stretch"
                             :background-color "#654F6F"
                             :padding 10
                             :border-radius 5}}
   :password-text {:style {:color "whitesmoke"
                           :margin 10
                           :font-size 25
                           :align-self "center"
                           :font-weight "bold"}}
   :page-name-text {:style {:align-self "center"
                            :font-size 20
                            :font-weight "100"
                            :padding-top 30
                            :color "#654F6F"}}
   :app-root-view {:style {:flex-direction "column"
                           :align-items "center"
                           :background-color "whitesmoke"}}
   :swiper {:shows-pagination true
            :pagination-style {:bottom 160}}})

(ns wordsmith-cljs.components.buttons
  (:require
   [reagent.core :as reagent])
  (:require-macros
   [devcards.core :as dc :refer [defcard]]))

(defn- get-button-class
  "Helper function for mapping button type to class"
  [button-type]
  (case button-type
    :success "button button--success"
    :danger "button button--danger"
    "button"))

(defn button [label click-handler button-type disabled]
  "Create a button component"
  [:button {:class (get-button-class button-type)
            :on-click click-handler
            :disabled disabled} label])

(defn button-set [buttons]
  [:div {:class "button-set"}
   (for [button-def buttons]
     ^{:key button-def} (button
                         (:label button-def)
                         (:on-click button-def)
                         (:button-type button-def)
                         (:disabled button-def)))])

(defcard
  "Lorem ipsum dolor sit amet"
  (fn [data-atom _] (reagent/as-element [:div
                               (button "Normal button" #(reset! data-atom {:last-click "Normal button clicked"}) nil false)
                               (button "Success button" #(reset! data-atom {:last-click "Success button clicked"}) :success false)
                               (button "Danger button" #(reset! data-atom {:last-click "Danger button clicked"}) :danger false)
                               (button "Disabled button" #(reset! data-atom {:last-click "Disabled button clicked"}) nil true)
                               [:br] [:br]]))
  {:last-click nil}
  {:inspect-data true})

(defcard
  "Lorem ipsum dolor sit amet"
  (fn [data-atom _] (reagent/as-element [:div
                               (button-set [{:label "Button 1" :on-click #(reset! data-atom {:last-click "Button 1 clicked"}) :button-type :success :disabled false}
                                            {:label "Button 2" :on-click #(reset! data-atom {:last-click "Button 2 clicked"}) :disabled false}
                                            {:label "Button 3" :on-click #(reset! data-atom {:last-click "Button 3 clicked"}) :disabled true}])
                               [:br]]))
  {:last-click nil}
  {:inspect-data true})
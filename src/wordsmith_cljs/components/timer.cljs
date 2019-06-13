(ns wordsmith-cljs.components.timer
  (:require
   [reagent.core :as reagent])
  (:require-macros
   [devcards.core :as dc :refer [defcard]]))

(defn timer [value]
  (let [rotation (* 360 value)]
    [:div {:class "timer-container"}
     [:div
      [:svg {:width "200px" :height "200px"}
       [:circle {:cx "100px" :cy "100px" :r "90px"}]
       [:path {:d "M100 100 L100 30" :transform (str "rotate (" rotation " 100 100)")}]]]]))


(defcard
  "# Timer
Displaying how far through the allowed time the player is")

(defn- timer-debug-input
  "Helper function for the timer dev card to allow the user to twiddle with the input value"
  [data-atom]
  [:div
   [:input {:value (:value @data-atom)
            :type "range" :min 0 :max 1 :step 0.01 :name "timer-debug-input"
            :on-change (fn [e] (reset! data-atom {:value (cljs.reader/read-string (-> e .-target .-value))}))}]])

(defcard
  "The `timer` component takes a single float between `0.0` and `1.0` to dictate how far through the allowed time the player is.
This component is only responsible for the display, duration (and how far the player has progressed) needs to be
handled elsewhere and the final value passed in.
"
  (fn [data-atom _] (reagent/as-element
                     [:div
                      (timer-debug-input data-atom)
                      (timer (:value @data-atom))]))
  {:value 0}
  {:inspect-data true})
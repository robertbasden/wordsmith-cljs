(ns wordsmith-cljs.components
  (:require
   [sablono.core :as sab :include-macros true])
  (:require-macros
   [devcards.core :as dc :refer [defcard deftest]]))

(defn page-title
  "Styling for page title"
  [text]
  [:h1 {:class "page-title"} text])

(defn page-text
  "Styling for page text"
  [text]
  [:div {:class "page-text"} text])

(defn instructions-container
  "Styling for instructions container"
  [text]
  [:div {:class "instructions-container"} text])

(defn hints-container
  "Styling for hints container"
  [text]
  [:div {:class "hint-container"} text])

(defn- get-button-class
  "Helper function for mapping button type to class"
  [button-type]
  (case button-type
    :success "button button--success"
    :danger "button button--danger"
    "button"))

(defn button [label click-handler button-type disabled]
  "Create a button component"
  [:button {
            :class (get-button-class button-type)
            :on-click click-handler
            :disabled disabled } label])

(defn button-set [buttons]
  [:div {:class "button-set"}
   (for [button-def buttons]
     ^{:key button-def} (button
                         (:label button-def)
                         (:on-click button-def)
                         (:button-type button-def)
                         (:disabled button-def)))])

(defn timer [value]
  (let [rotation (* 360 value)]
    [:div {:class "timer-container"}
     [:div
      [:svg {:width "200px" :height "200px"}
       [:circle {:cx "100px" :cy "100px" :r "90px"}]
       [:path {:d "M100 100 L100 30" :transform (str "rotate (" rotation " 100 100)")}]]]]))

(defn letter-display [letters]
  "This component is used for displaying letters, in this case tge letters can not be selected"
  [:div {:class "letter-container"}
   (map-indexed (fn [idx letter] [:div {:class "letter no-select" :key idx} [:div letter]]) letters)])

(defn letter-select [letters click-handler]
  "This component is used for displaying letters, which can the be selected / disabled / etc..."
  [:div {:class "letter-container"}
   (map-indexed (fn [idx l]
                  (let [classes (if (nil? (:selection l)) "letter no-select" "letter letter--disabled no-select")]
                    [:div {:class classes :key idx :on-click (fn [] (if (nil? (:selection l)) (click-handler l)))}
                     [:div (:letter l)]
                     ])) letters)])

;; cards

(defcard
  (sab/html
   (page-title "Page title")))

(defcard
  (sab/html
   (page-text "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc vel ex fringilla, dapibus nisi quis, vulputate quam. Suspendisse vestibulum consequat velit nec dignissim.")))

(defcard
  (sab/html
   (instructions-container "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc vel ex fringilla, dapibus nisi quis, vulputate quam. Suspendisse vestibulum consequat velit nec dignissim.")))

(defcard
  (sab/html
   (hints-container "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc vel ex fringilla, dapibus nisi quis, vulputate quam. Suspendisse vestibulum consequat velit nec dignissim.")))

(defcard
  "Lorem ipsum dolor sit amet"
  (fn [data-atom _] (sab/html [:div 
                               (button "Normal button" #(reset! data-atom { :last-click "Normal button clicked" }) nil false)
                               (button "Success button" #(reset! data-atom {:last-click "Success button clicked"}) :success false)
                               (button "Danger button" #(reset! data-atom {:last-click "Danger button clicked"}) :danger false)
                               (button "Disabled button" #(reset! data-atom {:last-click "Disabled button clicked"}) nil true)
                               [:br][:br]]))
  { :last-click nil }
  {:inspect-data true})

(defcard
  "Lorem ipsum dolor sit amet"
  (fn [data-atom _] (sab/html [:div
                               (button-set [{:label "Button 1" :on-click #(reset! data-atom {:last-click "Button 1 clicked"}) :button-type :success :disabled false}
                                            {:label "Button 2" :on-click #(reset! data-atom {:last-click "Button 2 clicked"}) :disabled false}
                                            {:label "Button 3" :on-click #(reset! data-atom {:last-click "Button 3 clicked"}) :disabled true}])
                               [:br]]))
  {:last-click nil}
  {:inspect-data true})

(defn- timer-debug-input [data-atom]
  [:div
   [:input {:value (:value @data-atom)
            :type "range" :min 0 :max 1 :step 0.01 :name "timer-debug-input"
            :on-change (fn [e] (reset! data-atom {:value (cljs.reader/read-string (-> e .-target .-value))}))}]
   [:label { :for "timer-debug-input" } "Elapsed time"]])

(defcard
  "
# Timer
The time component just takes a single float between `0.0` and `1.0` to dictate how far through the allowed time the player is.
"
  (fn [data-atom _] (sab/html [:div
                               (timer-debug-input data-atom)
                               (timer (:value @data-atom))]))
  {:value 0}
  {:inspect-data true})

(defcard
  "
# Letter Display
Letter display component just takes the word to display (as a single `string`):
```
(letter-display \"WORD\")
```
results in:
"
  (sab/html
   (letter-display "WORD")))

(def selected-letters
  [{:id 1 :letter "W" :selection nil}
   {:id 2 :letter "O" :selection nil}
   {:id 3 :letter "R" :selection nil}
   {:id 4 :letter "D" :selection nil}])

(defn update-selected [letter data-atom]
  (swap! data-atom assoc :last-letter-clicked letter))

(defcard
  "
# Letter Selection
For displaying the letters the player has selected, we need to use the more advanced component, as letters
will need to have click events bound and also be disabled when they are selected. The click handler provided
will be called with the full map of the letter that is clicked.
"
  (fn [data-atom _] (sab/html 
                     [:div
                      (letter-select selected-letters (fn [letter]
                                                        (update-selected letter data-atom)))
                      [:br]]))
  {:selected-letters selected-letters :last-letter-clicked nil }
  {:inspect-data true})
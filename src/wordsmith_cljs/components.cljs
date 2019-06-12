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
     ^{:key button-def} [button
                         (:label button-def)
                         (:on-click button-def)
                         (:button-type button-def)
                         (:disabled button-def)])])

(defn timer [value]
  (let [rotation (* 360 value)]
    [:div {:class "timer-container"}
     [:div
      [:svg {:width "200px" :height "200px"}
       [:circle {:cx "100px" :cy "100px" :r "90px"}]
       [:path {:d "M100 100 L100 30" :transform (str "rotate (" rotation " 100 100)")}]]]]))

(defn letter-select [letters click-handler]
  "This component is used for displaying letters, which can the be selected / disabled / etc..."
  [:div {:class "letter-container"}
   (map-indexed (fn [idx l]
                  (let [classes (if (nil? (:selection l)) "letter no-select" "letter letter--disabled no-select")]
                    [:div {:class classes :key idx :on-click (fn [] (if (nil? (:selection l)) (click-handler l)))}
                     [:div (:letter l)]
                     ])) letters)])

(defn letter-display [letters]
  "This component is used for displaying letters, in this case tge letters can not be selected"
  [:div {:class "letter-container"}
   (map-indexed (fn [idx letter] [:div {:class "letter no-select" :key idx} [:div letter]]) letters)])

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
                               (button-set [{:label "Normal button" :on-click #(reset! data-atom {:last-click "Normal button clicked"}) :button-type nil :disabled false}
                                            {:label "Normal button" :on-click #(reset! data-atom {:last-click "Normal button clicked"}) :button-type nil :disabled false}])]))
  {:last-click nil}
  {:inspect-data true})

(defn- timer-debug-input [data-atom]
  [:input {:value (:value @data-atom)
           :type "range" :min 0 :max 1 :step 0.01
           :on-change (fn [e] (reset! data-atom {:value (cljs.reader/read-string (-> e .-target .-value))}))}])

(defcard
  "Lorem ipsum dolor sit amet"
  (fn [data-atom _] (sab/html [:div 
                               (timer-debug-input data-atom)
                               (timer (:value @data-atom))]))
  { :value 0 }
  { :inspect-data true })

(def test-available-letters
  [{:id 1 :letter "W" :selection 1}
   {:id 2 :letter "O" :selection 2}
   {:id 3 :letter "R" :selection 3}
   {:id 4 :letter "D" :selection 4}])

(defcard
  (sab/html
   (letter-select test-available-letters #())))

(defcard
  (sab/html
   (letter-display "WORD")))
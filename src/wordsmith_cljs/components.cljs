(ns wordsmith-cljs.components
  (:require
   [sablono.core :as sab :include-macros true])
  (:require-macros
   [devcards.core :as dc :refer [defcard deftest]]))

(defn page-title [text]
  [:h1 {:class "page-title"} text])

(defn page-text [text]
  [:div {:class "page-text"} text])

(defn button [text]
  [:button {:class "button button--success"} text])

(defn button-set [buttons]
  [:div {:class "button-set"} buttons])

(defn instructions-container [text]
  [:div {:class "instructions-container"} text])

(defn hints-container [text]
  [:div {:class "hint-container"} text])

(defn timer [value]
  (let [rotation (* 360 value)]
    [:div {:class "timer-container"}
     [:div
      [:svg {:width "200px" :height "200px"}
       [:circle {:cx "100px" :cy "100px" :r "90px"}]
       [:path {:d "M100 100 L100 30" :transform (str "rotate (" rotation " 100 100)")}]]]]))

(defn letter-element [letter classes]
  [:div {:class classes}
   [:div letter]])

(defn letter
  ([letter] (letter-element letter "letter no-select"))
  ([letter disabled?]
   (if (= disabled? true)
     (letter-element letter "letter no-select letter--disabled")
     (letter-element letter "letter no-select"))))

;;letter-container--selectable
(defn letter-container [letters]
  [:div {:class "letter-container"} letters])

;; cards

(defcard page-title
  (sab/html
   (page-title "Lorem ipsum dolor sit amet")))

(defcard page-text
  (sab/html
   (page-text "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc vel ex fringilla, dapibus nisi quis, vulputate quam. Suspendisse vestibulum consequat velit nec dignissim.")))

(defcard button
  (sab/html
   (button "Start Game")))

(defcard button-set
  (sab/html
   (button-set [(button "Undo")
                (button "Clear")
                (button "Submit")
                (button "Show Hint")])))

(defcard instructions-container
  (sab/html
   (instructions-container "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc vel ex fringilla, dapibus nisi quis, vulputate quam. Suspendisse vestibulum consequat velit nec dignissim.")))

(defcard hints-container
  (sab/html
   (hints-container "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc vel ex fringilla, dapibus nisi quis, vulputate quam. Suspendisse vestibulum consequat velit nec dignissim.")))

(defn timer-debug-input [data-atom]
  [:input {:value (:value @data-atom)
           :type "range" :min 0 :max 1 :step 0.01
           :on-change (fn [e] (reset! data-atom {:value (cljs.reader/read-string (-> e .-target .-value))}))}])

(defcard timer
  "Lorem ipsum dolor sit amet"
  (fn [data-atom _] (sab/html [:div 
                               (timer-debug-input data-atom)
                               (timer (:value @data-atom))]))
  { :value 0 }
  { :inspect-data true })

(defcard letter
  (sab/html
   [:div
    (letter "A" false)
    (letter "A" true)]))

(defcard letter-container
  (sab/html
   (letter-container [(letter "A")
                      (letter "A")])))

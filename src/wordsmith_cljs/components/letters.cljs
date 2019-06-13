(ns wordsmith-cljs.components.letters
  (:require
   [reagent.core :as reagent])
  (:require-macros
   [devcards.core :as dc :refer [defcard]]))

(defn display [letters]
  "This component is used for displaying letters, in this case tge letters can not be selected"
  [:div {:class "letter-container"}
   (map-indexed (fn [idx letter] [:div {:class "letter no-select" :key idx} [:div letter]]) letters)])

(defn select [letters click-handler]
  "This component is used for displaying letters, which can the be selected / disabled / etc..."
  [:div {:class "letter-container"}
   (map-indexed (fn [idx l]
                  (let [classes (if (nil? (:selection l)) "letter no-select" "letter letter--disabled no-select")]
                    [:div {:class classes :key idx :on-click (fn [] (if (nil? (:selection l)) (click-handler l)))}
                     [:div (:letter l)]])) letters)])

(defn- update-selected
  "Helper function for the selection dev card to shpw the click handler in action"
  [letter data-atom]
  (swap! data-atom assoc :last-letter-clicked letter))

(defcard
  "
# Letter Display
Letter display component just takes the word to display (as a single `string`):
```
(letter-display \"WORD\")
```
results in:
"
  (reagent/as-element
   (display "WORD")))

(defcard
  "
# Letter Selection
For displaying the letters the player has selected, we need to use the more advanced component, as letters
will need to have click events bound and also be disabled when they are selected. The click handler provided
will be called with the full map of the letter that is clicked.
"
  (fn [data-atom _] (reagent/as-element
                     [:div
                      (select (:selected-letters @data-atom) (fn [letter]
                                                        (update-selected letter data-atom)))
                      [:br]]))
  {:selected-letters [{:id 1 :letter "W" :selection nil}
                      {:id 2 :letter "O" :selection nil}
                      {:id 3 :letter "R" :selection nil}
                      {:id 4 :letter "D" :selection nil}]
   :last-letter-clicked nil}
  {:inspect-data true})
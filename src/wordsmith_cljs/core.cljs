(ns wordsmith-cljs.core
  (:require
   [sablono.core :as sab :include-macros true]
   [wordsmith-cljs.components :as components])
  (:require-macros
   [devcards.core :as dc :refer [defcard deftest]]))

(enable-console-print!)

(defn page [contents]
  [:div { :class "container" } contents])

;; pages 

(defn title-page []
  (page [(components/page-title "WORDSMITH")
         (components/page-text "Complete the anagram before the time runs out")
         (components/button-set [(components/button "Start Game")])]))

(defn game []
  (page [(components/timer 0)
         (components/letter-container [(components/letter "A")])]))

(defn game-over []
  (page [(components/page-title "Whoops!")
         (components/page-text "Whoops! You ran out of time!")
         (components/button-set [(components/button "Start Again?")])]))

(defn main []
  ;; conditionally start the app based on whether the #main-app-area
  ;; node is on the page
  (if-let [node (.getElementById js/document "main-app-area")]
    (.render js/ReactDOM (sab/html
                          (title-page)
                          ) node)))

(main)
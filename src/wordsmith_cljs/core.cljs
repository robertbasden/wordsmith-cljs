(ns wordsmith-cljs.core
  (:require
   [sablono.core :as sab :include-macros true]
   [reagent.core :as r]
   [wordsmith-cljs.components :as components])
  (:require-macros
   [devcards.core :as dc :refer [defcard deftest]]))

(enable-console-print!)

;; game logic

(def allowed-time (* 10 1000))

(defn create-new-game-state [current-time]
  { :end-time (+ current-time allowed-time) })

(defn change-page [current-state page]
  (assoc current-state
                         :current-page page
                         :game-state (if (= page :game-in-progress)
                                       (create-new-game-state (:current-time current-state))
                                       nil)))

(defn tick [current-state time]
     (if (and
          (not (nil? (:game-state current-state)))
          (> (:current-time current-state)
             (get-in current-state [:game-state :end-time])))
       (assoc current-state
              :current-time time
              :game-state nil
              :current-page :game-over)
       (assoc current-state
              :current-time time)))

(def state (r/atom {:current-page :title-screen
                  :game-state nil
                  :current-time 0
                  }))

;; page components

(defn title-screen []
  [:div 
   (components/page-title "WORDSMITH")
   (components/page-text "Complete the anagram before the time runs out")
   (components/button-set [{:label "Start" :on-click #(swap! state change-page :game-in-progress)}])])

(defn game-in-progress []
  (let [current-time (:current-time @state)
        end-time (get-in @state [:game-state :end-time])
        time-remaining (- end-time current-time)
        fraction-gone (- 1 (/ time-remaining allowed-time))]
    [:div 
     (components/timer fraction-gone)
     (components/letter-container [[components/letter "A"]])]))

(defn game-over []
  [:div
   (components/page-title "Whoops!")
   (components/page-text "Whoops! You ran out of time!")
   (components/button-set [{:label "Start Again?" :on-click #(swap! state change-page :game-in-progress)}])])

(defn main-component []
  [:div {:class "container"}
   (case (:current-page @state)
     :game-over [game-over]
     :game-in-progress [game-in-progress]
     [title-screen])])

(defn update-time
  "Use request animation frame to keep the time stored in the game state up to date on every tick"
  [time]
  (.requestAnimationFrame js/window update-time)
  (swap! state tick time))

;; Start the request animation frame function to keep time up to date
(update-time 0)

(defn main []
  ;; conditionally start the app based on whether the #main-app-area
  ;; node is on the page
  (if-let [node (.getElementById js/document "main-app-area")]
    (r/render [main-component] node)))

(main)
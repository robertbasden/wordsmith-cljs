(ns wordsmith-cljs.core
  (:require
   [clojure.string :as str]
   [sablono.core :as sab :include-macros true]
   [reagent.core :as r]
   [wordsmith-cljs.game :as game]
   [wordsmith-cljs.components :as components])
  (:require-macros
   [devcards.core :as dc :refer [defcard deftest]]))

(enable-console-print!)

;; game logic

(def allowed-time (* 10 1000))

(defn change-page [current-state page message]
  (assoc current-state
         :current-page page
         :message message
         :game-state (if (= page :game-in-progress)
                       (game/create-new-game "MAXIMIZED" "Hint goes here" (:current-time current-state) allowed-time)
                       nil)))

(defn tick [current-state time]
     (if (and
          (not (nil? (:game-state current-state)))
          (> (:current-time current-state)
             (get-in current-state [:game-state :end-time])))
       (assoc current-state
              :current-time time
              :game-state nil
              :current-page :game-over
              :message "Whoops! You ran out of time!")
       (assoc current-state
              :current-time time)))

(defn select-letter [{:keys [game-state] :as current-state} letter]
  (if (not (nil? game-state))
    (assoc current-state :game-state (game/select-by-letter game-state letter))
    current-state))

(defn select-by-character [{:keys [game-state] :as current-state} character]
  (if (not (nil? game-state))
    (assoc current-state :game-state (game/select-by-character game-state character))
    current-state))

(defn show-hint [{:keys [game-state] :as current-state}]
  (if (not (nil? game-state))
    (assoc current-state :game-state (game/show-hint game-state))
    current-state))

(defn clear-current-guess [{:keys [game-state] :as current-state}]
  (if (not (nil? game-state))
    (assoc current-state :game-state (game/reset-current-guess game-state))
    current-state))

(defn undo-last-letter [{:keys [game-state] :as current-state}]
  (if (not (nil? game-state))
    (assoc current-state :game-state (game/undo game-state))
    current-state))

(defonce state (r/atom {:current-page :title-screen
                  :game-state nil
                  :current-time 0
                  }))

(defn next-stage [current-state]
  (if (game/solution-correct? (get current-state :game-state))
    (assoc current-state :game-state (game/create-new-game "MAXIMIZED" "Hint goes here" (:current-time current-state) allowed-time))
    (change-page current-state :game-over "Whoops! That was the wrong word!")))

;; page components

(defn title-screen []
  [:div 
   (components/page-title "WORDSMITH")
   (components/page-text "Complete the anagram before the time runs out")
   (components/button-set [{:label "Start" :on-click #(swap! state change-page :game-in-progress) :button-type :success}])])

(defn game-in-progress []
  (let [current-time (:current-time @state)
        available-letters (get-in @state [:game-state :available-letters])
        current-guess (->> available-letters
                           (filter (fn [l] (not (nil? (:selection l)))))
                           (sort-by :selection)
                           (map (fn [l] (:letter l))))
        end-time (get-in @state [:game-state :end-time])
        time-remaining (- end-time current-time)
        fraction-gone (- 1 (/ time-remaining allowed-time))
        can-undo (> (count current-guess) 0)
        can-clear (> (count current-guess) 0)
        can-submit (= (count (filter (fn [l] (nil? (:selection l))) available-letters)) 0)
        can-show-hint (not (get-in @state [:game-state :show-hint]))
        letter-selection-started (> (count current-guess) 0)]
    [:div
     (components/timer fraction-gone)
     (components/letter-select available-letters #(swap! state select-letter %1))
     [:br]
     (if (= letter-selection-started true)
       (components/letter-display current-guess)
       [:div {:class "instructions-container"} "Click or type the letters above to solve the anagram before the time runs out!"])
     [:br]
     (components/button-set [{:label "Undo" :on-click #(swap! state undo-last-letter) :disabled (not can-undo)}
                             {:label "Clear" :on-click #(swap! state clear-current-guess) :button-type :danger :disabled (not can-clear)}
                             {:label "Submit" :on-click #(swap! state next-stage) :button-type :success :disabled (not can-submit)}
                             {:label "Show Hint" :on-click #(swap! state show-hint) :disabled (not can-show-hint)}])
     [:br]
     (if (get-in @state [:game-state :show-hint])
       [:div {:class "hint-container"} (str "Hint: " (get-in @state [:game-state :hint]))]
       [:div])]))

(defn game-over [message]
  [:div
   (components/page-title "Whoops!")
   (components/page-text message)
   (components/button-set [{:label "Start Again?" :on-click #(swap! state change-page :game-in-progress) :button-type :success}])])

(defn main-component []
  [:div {:class "container"}
   (case (:current-page @state)
     :game-over [game-over (:message @state)]
     :game-in-progress [game-in-progress]
     [title-screen])])

(defn update-time
  "Use request animation frame to keep the time stored in the game state up to date on every tick"
  [time]
  (.requestAnimationFrame js/window update-time)
  (swap! state tick time))

;; Start the request animation frame function to keep time up to date
(update-time 0)

;; This function has to be definied using `defonce` so that we retain the reference for add / removing
;; the same event listener, rather than getting a new reference every time
(defonce handle-keypress (fn [e] (let [keyCode (.-keyCode e)
                                       character (.-key e)]
                                   (if (= keyCode 8)
                                     (swap! state undo-last-letter)
                                     (swap! state select-by-character (clojure.string/upper-case character))))))

;; If we reload the page there is a danger we will add multiple event listeners
;; to counter this we just remove any existing first and then re-add
(.removeEventListener js/window "keydown" handle-keypress)
(.addEventListener js/window "keydown" handle-keypress)

(defn main []
  ;; conditionally start the app based on whether the #main-app-area
  ;; node is on the page
  (if-let [node (.getElementById js/document "main-app-area")]
    (r/render [main-component] node)))

(main)
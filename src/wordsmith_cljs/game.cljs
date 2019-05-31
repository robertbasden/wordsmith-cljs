(ns wordsmith-cljs.game
  (:require
   [clojure.string :as str]
   [cljs.test :refer [is testing]])
  (:require-macros
   [devcards.core :as dc :refer [defcard deftest]]))

;; Create new game

(defn- map-word [word]
  (->> (str/split word "")
       (map-indexed (fn [idx letter] {:id (+ idx 1) :letter letter :selection nil}))
       (shuffle)))

(defn create-new-game [word hint current-time allowed-time]
  {:end-time (+ current-time allowed-time)
   :available-letters (map-word word)
   :original-word word
   :hint hint
   :next-selection 1
   :show-hint false})

(defcard create-new-game
  (create-new-game "Lorem" "Lorem ipsum dolor sit amet, consectetur adipiscing elit" 1000 1000))

;; Show hint

(defn show-hint [current-state]
  (assoc current-state :show-hint true))

(defcard show-hint
  {})

(deftest show-hint-tests
  (testing "Show hint tests"
    (is (= (+ 3 4) 7) "Testing the adding")
    (is (= (+ 1 2) 3) "Testing the adding")))

;; Add next letter

(defn add-next-letter []
  {})

(defcard add-next-letter
  {})

;; Reset current guess

(defn reset-current-guess [current-state]
  (assoc current-state
         :available-letters (map (fn [l] (assoc l :selection nil)) (get current-state :available-letters))
         :next-selection 1))

(defcard reset-current-guess
  {})

;; Undo last letter

(defn undo [{:keys [next-selection available-letters] :as current-state}]
  (let [new-next-selection (- next-selection 1)]
    (assoc current-state
           :available-letters (map (fn [l]
                                     (if (= (:selection l) new-next-selection)
                                       (assoc l :selection nil)
                                       l)
                                     ) available-letters)
           :next-selection new-next-selection)))

(defcard undo
  {})

;; Check solution

(defn solution-correct? [{:keys [available-letters original-word]}]
  (let [guessed-word (->> available-letters
                          (sort-by :selection)
                          (map (fn [l] (:letter l)))
                          (clojure.string/join))]
    (= guessed-word original-word)))

(defcard solution-correct?
  {})
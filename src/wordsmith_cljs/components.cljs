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
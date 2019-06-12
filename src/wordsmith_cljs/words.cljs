(ns wordsmith-cljs.words)

(def words [
             ["RECOGNIZE" "To know someone or something because you have seen or heard him or her or experienced it before"]
             ["FIRM" "Well fixed in place or position"]
             ["PRECEDENT" "An action, situation, or decision that has already happened and can be used as a reason why a similar action or decision should be performed or made"]
             ["RABBIT" "A small animal with long ears and large front teeth that moves by jumping on its long back legs"]
             ["BARREL" "A large container, made of wood, metal, or plastic, with a flat top and bottom and curved sides that make it fatter in the middle"]])

(defn get-random-word []
  (-> words
      shuffle
      first))
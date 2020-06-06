(ns tictactoe.core)

(def board [1 2 3 4 5 6 7 8 9])

(def player-seq 
  "infinite lazy seq"
  (cycle [:x :o]))  

(def valid-mode [1 2]) 

(defn print-board
  "display current board state"
  [board]
  (println (nth board 0) (nth board 1) (nth board 2))
  (println (nth board 3) (nth board 4) (nth board 5))
  (println (nth board 6) (nth board 7) (nth board 8)))


(defn rows
  "return all comb of rows"
  [board]
  (concat 
    (partition-all 3 board)   ;(1 2 3) (4 5 6) (7 8 9)
    (list 
      (take-nth 3 board)  ;(1 4 7)
      (take-nth 3 (drop 1 board)) ;(2 5 8)
      (take-nth 3 (drop 2 board)) ;(3 6 9)
      (take-nth 4 board)  ;(1 5 9)
      (take-nth 2 (drop 2 (drop-last 2 board))))))  ;(3 5 9)

(defn winner-in-row?
  "check if there is a winner in a particular row"
  [row]
  (if (every? #{:x} row) :x
    (if (every? #{:o} row) :o)))    

(defn winner? 
  "return winner else nil"
  [board]
  (first (filter #{:x :o} (map winner-in-row? (rows board)))))

(defn board-full?
  "check if entire board full or not"
  [board]
  (every? #{:o :x} board))

(defn next-move-human
  [board]
  (let [input
    (try 
      (. Integer parseInt (read-line))
      (catch Exception e nil))]
    (if (some #{input} board)
    input
    nil)))

(defn get-occurances 
  [row player]
  (if (get (frequencies row) player) (get (frequencies row) player)
  0))

(defn blank
  [row]
  (< (+ (get-occurances row :x) (get-occurances row :o)) 3))

(defn find-blank
  [row player]
  (first (filter #(not (= player %)) row)))   

(defn win-comp?
  [row]
  (if (= 2 (get-occurances row :o))
    (if (blank row) (find-blank row :o))))  

(defn win-human?
  [row]
  (if (= 2 (get-occurances row :x))
    (if (blank row) (find-blank row :x))))  
      
(defn check-win-comp
  [board]
  (first (filter #(not (= nil %)) (map win-comp? (rows board)))))  

(defn check-win-human
  [board]
  (first (filter #(not (= nil %)) (map win-human? (rows board)))))  
    
(defn predict-move-random
  [board]
  (if (first (filter #{5} board)) 
    5
    (if (first (filter #{1} board))
      1
      (if (first (filter #{9} board))
        (if (first (filter #{2} board))
          2 
          (if (first (filter #{9} board))
            7)
        )
        (if (first (filter #{9} board))
          9)))))
    
(defn predict-move
  [board]
  (if (not (= (check-win-comp board) nil)) 
    (check-win-comp board)
    (if (not (= (check-win-human board) nil)) 
      (check-win-human board)
      (predict-move-random board))))

(defn next-move-comp
  [board player]
  (if (= player :x)
    (next-move-human board)
    (predict-move board)))  

(defn next-move 
  [board mode-selected player]
  (if (= mode-selected 1)
    (next-move-human board)
    (if (= mode-selected 2)
      (next-move-comp board player))))

(defn make-move
  [player board mode-selected]
  (println "it is turn of " player)
  (loop [move (next-move board mode-selected player)]
    (if move 
      (assoc board (dec move) player)
      (do 
        (println "invalid move. Try again")
        (recur (next-move board mode-selected player))))))

(defn play-game
  "keep changing player till we have a winner or board fill"
  [board player-seq mode-selected]
  (loop [board board player-seq player-seq]
    (let [winner (winner? board)]
      (print-board board)
      (cond 
        winner (println winner " wins")
        (board-full? board) (println "draw")
        :else
        (recur 
          (make-move (first player-seq) board mode-selected)
          (rest player-seq))))))

(defn mode
  [valid-mode]
  (let [input
    (try 
      (. Integer parseInt (read-line))
      (catch Exception e nil))]
    (if (some #{input} valid-mode)
    input
    nil)))
    
(defn -main
  []
  (println "Type 1 for human v/s human")
  (println "Type 2 for computer v/s human")
  (loop [mode-selected (mode valid-mode)]
    (if (some #{mode-selected} valid-mode)
      (play-game board player-seq mode-selected)
      (do 
        (println "invalid mode. Choose from 1 or 2")
        (recur (mode valid-mode))))))
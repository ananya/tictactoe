(ns tictactoe.core-test
  (:require [clojure.test :refer :all]
            [tictactoe.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 0))))

(deftest initialise-matrix-test
  (testing "initialise empty matrix"
    (is (= [1 2 3 4 5 6 7 8 9] board))))

(deftest winner-in-row?-test
  (testing "winner in row"
    (is (= :x (winner-in-row? [:x :x :x]))
    (is (= nil (winner-in-row? [1 2 3]))))))

(deftest winner-test
  (is (= nil (winner? [1 2 3 4 5 6 7 8 9]))))
    
(deftest board-full?-test
  (is (= true (board-full? [:x :x :o :x :x :x :o :x :o]))))

(deftest winner-in-row?-test
  (testing "if line contains all x or o then winner"
    (is (= :x (winner-in-row? [:x :x :x])))))

(deftest get-occurances-test
  (testing "occurance count in row"
    (is (= 2 (get-occurances [1 :x :x] :x)))
    (is (= 0 (get-occurances [1 :x :x] :o)))))

(deftest blank-test
  (testing "find if there is a blank poss"
    (is (= true (blank [1 :x :x])))
    (is (= false (blank [:o :x :x])))))

(deftest find-blank-test
  (testing "find blank position"
    (is (= 1 (find-blank [1 :x :x] :x)))))

(deftest win-comp?-test
  (testing "return position where comp can win"
    (is (= 3 (win-comp? [:o :o 3])))))

(deftest win-human?-test
  (testing "return position where comp can win"
    (is (= 3 (win-human? [:x :x 3])))))
    
(deftest check-win-comp-test
  (testing "check if comp can win or not in current state"
    (is (= nil (check-win-comp [1 2 3 :x :x 6 7 8 9])))
    (is (= 1 (check-win-comp [1 :o :o :x :x 6 7 8 9])))))

(deftest check-win-human-test
  (testing "check if comp can win or not in current state"
    (is (= 6 (check-win-human [1 2 3 :x :x 6 7 8 9])))
    (is (= 6 (check-win-human [1 :o :o :x :x 6 7 8 9])))))
    
(deftest predict-move-test
  (testing "predict comp move"
    (is (= 4 (predict-move [1 2 3 4 :o :o 7 8 9])))
    (is (= 2 (predict-move [:o 2 3 4 :x :o 7 8 9])))))
package com.example.checkers.ai;

import com.example.checkers.model.Board;
import com.example.checkers.model.Piece;

public interface AIStrategy {
    // Метод для вибору найкращого ходу
    Move chooseMove(Board board, Piece.Color aiColor);

    // Внутрішній клас для представлення ходу
    class Move {
        public int fromRow;
        public int fromCol;
        public int toRow;
        public int toCol;

        public Move(int fromRow, int fromCol, int toRow, int toCol) {
            this.fromRow = fromRow;
            this.fromCol = fromCol;
            this.toRow = toRow;
            this.toCol = toCol;
        }
    }

    // Рівні складності ШІ
    enum Difficulty {
        EASY,   // Випадкові ходи
        MEDIUM, // Прості тактичні рішення
        HARD    // Глибокий аналіз та прогнозування
    }
}
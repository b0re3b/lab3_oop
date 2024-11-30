package com.example.checkers.ai;

import com.example.checkers.model.Board;
import com.example.checkers.model.Piece;
import com.example.checkers.model.Player;
import com.example.checkers.utils.MoveValidator;

public class ComputerPlayer extends Player {
    private AIStrategy strategy;
    private AIStrategy.Difficulty difficulty;

    // Конструктор
    public ComputerPlayer(String name, Piece.Color color,
                          AIStrategy strategy,
                          AIStrategy.Difficulty difficulty) {
        super(name, color);
        this.strategy = strategy;
        this.difficulty = difficulty;
    }

    @Override
    public boolean makeMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        // Вибираємо хід за допомогою стратегії ШІ
        AIStrategy.Move bestMove = strategy.chooseMove(board, getColor());

        // Перевіряємо валідність ходу
        if (bestMove != null &&
                MoveValidator.isValidMove(board, this,
                        bestMove.fromRow, bestMove.fromCol,
                        bestMove.toRow, bestMove.toCol)) {

            // Виконуємо фізичне переміщення шашки
            Piece piece = board.getPieceAt(bestMove.fromRow, bestMove.fromCol);

            // Видаляємо шашку зі старої позиції
            board.removePieceAt(bestMove.fromRow, bestMove.fromCol);

            // Встановлюємо шашку на нову позицію
            board.setPieceAt(bestMove.toRow, bestMove.toCol, piece);

            // Перевірка на захоплення шашок
            checkAndCapturePieces(board, bestMove);

            return true;
        }

        return false;
    }

    // Метод для перевірки та захоплення шашок суперника
    private void checkAndCapturePieces(Board board, AIStrategy.Move move) {
        // Логіка захоплення шашок
        int capturedRow = (move.fromRow + move.toRow) / 2;
        int capturedCol = (move.fromCol + move.toCol) / 2;

        Piece capturedPiece = board.getPieceAt(capturedRow, capturedCol);

        if (capturedPiece != null &&
                capturedPiece.getColor() != getColor()) {

            board.removePieceAt(capturedRow, capturedCol);
            incrementCapturedPieces();
        }
    }

    // Додаткові методи для налаштування складності ШІ
    public void setDifficulty(AIStrategy.Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public AIStrategy.Difficulty getDifficulty() {
        return difficulty;
    }
}
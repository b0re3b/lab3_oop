package com.example.checkers.ai;

import com.example.checkers.model.Board;
import com.example.checkers.model.Piece;
import com.example.checkers.model.Player;
import com.example.checkers.utils.MoveValidator;

public class ComputerPlayer extends Player {
    private final BasicStrategy strategy; // Використовуємо BasicStrategy

    // Конструктор
    public ComputerPlayer(String name, Piece.Color color, BasicStrategy basicStrategy) {
        super(name, color);
        this.strategy = new BasicStrategy(); // Ініціалізуємо конкретну стратегію
    }

    @Override
    public boolean makeMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
        // Вибираємо хід за допомогою BasicStrategy
        AIStrategy.Move bestMove = strategy.chooseMove(board, getColor());

        // Перевірка валідності ходу
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
        int capturedRow = (move.fromRow + move.toRow) / 2;
        int capturedCol = (move.fromCol + move.toCol) / 2;

        Piece capturedPiece = board.getPieceAt(capturedRow, capturedCol);

        if (capturedPiece != null &&
                capturedPiece.getColor() != getColor()) {

            board.removePieceAt(capturedRow, capturedCol);
            incrementCapturedPieces();
        }
    }
}

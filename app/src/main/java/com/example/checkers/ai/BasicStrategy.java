package com.example.checkers.ai;

import com.example.checkers.model.Board;
import com.example.checkers.model.Piece;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BasicStrategy implements AIStrategy {
    private final Random random = new Random();

    @Override
    public Move chooseMove(Board board, Piece.Color aiColor) {
        List<Move> possibleMoves = findPossibleMoves(board, aiColor);

        if (possibleMoves.isEmpty()) {
            return null;
        }

        // Випадковий вибір ходу
        return possibleMoves.get(random.nextInt(possibleMoves.size()));
    }

    // Пошук усіх можливих ходів для кольору
    private List<Move> findPossibleMoves(Board board, Piece.Color color) {
        List<Move> moves = new ArrayList<>();

        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {
                Piece piece = board.getPieceAt(fromRow, fromCol);

                if (piece != null && piece.getColor() == color) {
                    // Перевірка можливих напрямків руху
                    int[][] directions = piece.isKing()
                            ? new int[][]{{-1,-1}, {-1,1}, {1,-1}, {1,1}}  // King moves
                            : (color == Piece.Color.WHITE
                            ? new int[][]{{1,-1}, {1,1}}  // White moves down
                            : new int[][]{{-1,-1}, {-1,1}}  // Black moves up
                    );

                    for (int[] dir : directions) {
                        int toRow = fromRow + dir[0];
                        int toCol = fromCol + dir[1];

                        // Додаємо хід, якщо він можливий
                        if (isValidDestination(board, fromRow, fromCol, toRow, toCol)) {
                            moves.add(new Move(fromRow, fromCol, toRow, toCol));
                        }
                    }
                }
            }
        }

        return moves;
    }

    // Перевірка валідності destination
    private boolean isValidDestination(Board board,
                                       int fromRow, int fromCol,
                                       int toRow, int toCol) {
        // Перевірка меж дошки
        if (toRow < 0 || toRow >= 8 || toCol < 0 || toCol >= 8) {
            return false;
        }

        // Destination має бути порожнім
        return board.getPieceAt(toRow, toCol) == null;
    }
}
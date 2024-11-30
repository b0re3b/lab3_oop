package com.example.checkers.utils;

import com.example.checkers.model.Board;
import com.example.checkers.model.Piece;

public class GameRules {
    // Максимальна кількість ходів без захоплення шашок
    private static final int MAX_MOVES_WITHOUT_CAPTURE = 40;

    // Перевірка умов нічиєї
    public static boolean isDrawCondition(Board board) {
        return isStalemate(board) ||
                isInsufficientMaterial(board) ||
                isRepeatedPosition(board);
    }

    // Перевірка на pat (відсутність можливих ходів)
    private static boolean isStalemate(Board board) {
        // Перевіряємо, чи є можливі ходи для обох сторін
        return !hasValidMoves(board, Piece.Color.WHITE) &&
                !hasValidMoves(board, Piece.Color.BLACK);
    }

    // Перевірка на недостатню кількість шашок для перемоги
    private static boolean isInsufficientMaterial(Board board) {
        int whitePieces = countPieces(board, Piece.Color.WHITE);
        int blackPieces = countPieces(board, Piece.Color.BLACK);

        // Наприклад, якщо залишилось менше 3 шашок у кожного гравця
        return whitePieces <= 2 && blackPieces <= 2;
    }

    // Перевірка на повторення позиції
    private static boolean isRepeatedPosition(Board board) {
        // Складна логіка порівняння станів дошки
        // Потребує введення механізму збереження історії ходів
        return false; // Тимчасово
    }

    // Підрахунок шашок певного кольору
    private static int countPieces(Board board, Piece.Color color) {
        int count = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPieceAt(row, col);
                if (piece != null && piece.getColor() == color) {
                    count++;
                }
            }
        }
        return count;
    }

    // Перевірка наявності можливих ходів для кольору
    private static boolean hasValidMoves(Board board, Piece.Color color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPieceAt(row, col);
                if (piece != null && piece.getColor() == color) {
                    // Перевірка можливості руху для кожної шашки
                    if (canPieceMakeMove(board, row, col)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Перевірка можливості руху для конкретної шашки
    private static boolean canPieceMakeMove(Board board, int row, int col) {
        Piece piece = board.getPieceAt(row, col);
        if (piece == null) return false;

        // Напрямки руху залежать від типу шашки та її кольору
        int[][] directions = piece.isKing()
                ? new int[][]{{-1,-1}, {-1,1}, {1,-1}, {1,1}}
                : (piece.getColor() == Piece.Color.WHITE
                ? new int[][]{{1,-1}, {1,1}}
                : new int[][]{{-1,-1}, {-1,1}});

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            // Перевірка, чи можливий хід
            if (isValidMove(board, row, col, newRow, newCol)) {
                return true;
            }
        }

        return false;
    }

    // Перевірка валідності конкретного руху
    private static boolean isValidMove(Board board, int fromRow, int fromCol,
                                       int toRow, int toCol) {
        // Перевірка меж дошки
        if (toRow < 0 || toRow >= 8 || toCol < 0 || toCol >= 8) {
            return false;
        }

        // Destination має бути порожнім
        if (board.getPieceAt(toRow, toCol) != null) {
            return false;
        }

        return true;
    }
}
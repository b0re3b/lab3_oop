package com.example.checkers.utils;

import com.example.checkers.model.Board;
import com.example.checkers.model.Piece;
import com.example.checkers.model.Player;

public class MoveValidator {
    // Перевірка валідності ходу
    public static boolean isValidMove(Board board, Player player,
                                      int fromRow, int fromCol,
                                      int toRow, int toCol) {
        // Перевірка меж дошки
        if (!isWithinBoardBounds(fromRow, fromCol, toRow, toCol)) {
            return false;
        }

        Piece piece = board.getPieceAt(fromRow, fromCol);

        // Перевірка наявності шашки
        if (piece == null || piece.getColor() != player.getColor()) {
            return false;
        }

        // Перевірка напрямку руху
        if (!isValidMoveDirection(piece, fromRow, fromCol, toRow, toCol)) {
            return false;
        }

        // Перевірка відстані руху
        if (!isValidMoveDistance(piece, fromRow, fromCol, toRow, toCol)) {
            return false;
        }

        // Перевірка захоплення шашок
        return isValidCapture(board, piece, fromRow, fromCol, toRow, toCol);
    }

    // Перевірка меж дошки
    private static boolean isWithinBoardBounds(int fromRow, int fromCol,
                                               int toRow, int toCol) {
        return fromRow >= 0 && fromRow < 8 &&
                fromCol >= 0 && fromCol < 8 &&
                toRow >= 0 && toRow < 8 &&
                toCol >= 0 && toCol < 8;
    }

    // Перевірка напрямку руху
    private static boolean isValidMoveDirection(Piece piece,
                                                int fromRow, int fromCol,
                                                int toRow, int toCol) {
        // Для королеви дозволені всі напрямки
        if (piece.isKing()) {
            return Math.abs(fromRow - toRow) == Math.abs(fromCol - toCol);
        }

        // Для звичайних шашок - тільки вперед
        int direction = piece.getColor() == Piece.Color.WHITE ? 1 : -1;
        return (toRow - fromRow) == direction;
    }

    // Перевірка відстані руху
    private static boolean isValidMoveDistance(Piece piece,
                                               int fromRow, int fromCol,
                                               int toRow, int toCol) {
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);

        // Звичайний хід на одну клітинку
        if (rowDiff == 1 && colDiff == 1) {
            return true;
        }

        // Захоплення шашки (стрибок через шашку)
        return rowDiff == 2 && colDiff == 2;
    }

    // Перевірка захоплення шашок
    private static boolean isValidCapture(Board board, Piece piece,
                                          int fromRow, int fromCol,
                                          int toRow, int toCol) {
        // Якщо хід на одну клітинку - без захоплення
        if (Math.abs(fromRow - toRow) == 1) {
            return board.getPieceAt(toRow, toCol) == null;
        }

        // Перевірка захоплення шашки
        int capturedRow = (fromRow + toRow) / 2;
        int capturedCol = (fromCol + toCol) / 2;

        Piece capturedPiece = board.getPieceAt(capturedRow, capturedCol);

        // Має бути шашка іншого кольору
        return capturedPiece != null &&
                capturedPiece.getColor() != piece.getColor() &&
                board.getPieceAt(toRow, toCol) == null;
    }
}
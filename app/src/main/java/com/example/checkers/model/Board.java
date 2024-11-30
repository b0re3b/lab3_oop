package com.example.checkers.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final int BOARD_SIZE = 8;
    private final Piece[][] board;

    // Конструктор для ініціалізації дошки
    public Board() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];
        initializeBoard();
    }

    // Метод для initial setup дошки
    private void initializeBoard() {
        // Розставляємо initial pieces для білих
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if ((row + col) % 2 != 0) {
                    board[row][col] = new Piece(Piece.Color.WHITE);
                }
            }
        }

        // Розставляємо initial pieces для чорних
        for (int row = 5; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if ((row + col) % 2 != 0) {
                    board[row][col] = new Piece(Piece.Color.BLACK);
                }
            }
        }
    }

    // Перевірка, чи валідна позиція на дошці
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < BOARD_SIZE &&
                col >= 0 && col < BOARD_SIZE;
    }

    // Отримати piece з певної позиції
    public Piece getPieceAt(int row, int col) {
        if (isValidPosition(row, col)) {
            return board[row][col];
        }
        return null;
    }

    // Встановити piece на певну позицію
    public void setPieceAt(int row, int col, Piece piece) {
        if (isValidPosition(row, col)) {
            board[row][col] = piece;
        }
    }

    // Видалити piece з позиції
    public void removePieceAt(int row, int col) {
        if (isValidPosition(row, col)) {
            board[row][col] = null;
        }
    }

    // Перевірити, чи є pieces певного кольору
    public boolean hasPiecesForColor(Piece.Color color) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.getColor() == color) {
                    return true;
                }
            }
        }
        return false;
    }

    // Отримати можливі ходи для фігури на заданій позиції
    public List<int[]> getPossibleMoves(int row, int col) {
        List<int[]> possibleMoves = new ArrayList<>();
        Piece piece = getPieceAt(row, col);

        if (piece == null) {
            return possibleMoves; // Якщо немає фігури на цій клітинці, повертаємо порожній список
        }

        int direction = piece.getColor() == Piece.Color.WHITE ? -1 : 1; // Напрямок руху: для білих - вгору, для чорних - вниз

        // Перевірка можливих простих рухів (по діагоналі)
        // Зліва вгору (для білих) або справа вниз (для чорних)
        if (isValidPosition(row + direction, col - 1) && getPieceAt(row + direction, col - 1) == null) {
            possibleMoves.add(new int[]{row + direction, col - 1});
        }
        // Справа вгору (для білих) або зліва вниз (для чорних)
        if (isValidPosition(row + direction, col + 1) && getPieceAt(row + direction, col + 1) == null) {
            possibleMoves.add(new int[]{row + direction, col + 1});
        }

        // Перевірка можливих стрибків через фігури
        // Стрибок через ліву фігуру
        if (isValidPosition(row + 2 * direction, col - 2) && getPieceAt(row + direction, col - 1) != null && getPieceAt(row + direction, col - 1).getColor() != piece.getColor() && getPieceAt(row + 2 * direction, col - 2) == null) {
            possibleMoves.add(new int[]{row + 2 * direction, col - 2});
        }
        // Стрибок через праву фігуру
        if (isValidPosition(row + 2 * direction, col + 2) && getPieceAt(row + direction, col + 1) != null && getPieceAt(row + direction, col + 1).getColor() != piece.getColor() && getPieceAt(row + 2 * direction, col + 2) == null) {
            possibleMoves.add(new int[]{row + 2 * direction, col + 2});
        }

        return possibleMoves;
    }
}

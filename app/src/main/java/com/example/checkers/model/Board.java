package com.example.checkers.model;



public class Board {
    private static final int BOARD_SIZE = 8;
    private Piece[][] board;

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
}
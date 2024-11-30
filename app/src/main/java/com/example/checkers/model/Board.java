package com.example.checkers.model;

public class Board {
    private static final int BOARD_SIZE = 8;
    private Piece[][] board;

    public Board() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];
        initializeBoard();
    }

    private void initializeBoard() {
        // Розставляємо шашки на початку гри
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if ((row + col) % 2 != 0) {
                    board[row][col] = new Piece(false, row, col); // чорні шашки
                }
            }
        }

        for (int row = 5; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if ((row + col) % 2 != 0) {
                    board[row][col] = new Piece(true, row, col); // білі шашки
                }
            }
        }
    }

    public Piece getPiece(int row, int col) {
        return board[row][col];
    }

    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = board[fromRow][fromCol];
        piece.move(toRow, toCol);
        board[toRow][toCol] = piece;
        board[fromRow][fromCol] = null;
    }
}
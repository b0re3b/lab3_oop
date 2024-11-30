package com.example.checkers.model;

public class Piece {
    private boolean isWhite;  // колір шашки
    private boolean isKing;   // чи є шашка дамкою
    private int row;
    private int col;

    public Piece(boolean isWhite, int row, int col) {
        this.isWhite = isWhite;
        this.row = row;
        this.col = col;
        this.isKing = false;
    }

    // Геттери та сеттери
    public boolean isWhite() { return isWhite; }
    public boolean isKing() { return isKing; }
    public void makeKing() { this.isKing = true; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public void move(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
    }
}
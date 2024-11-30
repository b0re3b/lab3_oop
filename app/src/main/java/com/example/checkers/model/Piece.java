package com.example.checkers.model;


public class Piece {
    // Enum для кольорів шашок
    public enum Color {
        WHITE, BLACK
    }

    // Enum для типів шашок
    public enum Type {
        REGULAR, KING
    }

    private Color color;
    private Type type;
    private boolean isAlive;

    // Конструктор
    public Piece(Color color) {
        this.color = color;
        this.type = Type.REGULAR;
        this.isAlive = true;
    }

    // Getter для кольору
    public Color getColor() {
        return color;
    }

    // Getter для типу
    public Type getType() {
        return type;
    }

    // Перетворення на королеву
    public void kingMe() {
        this.type = Type.KING;
    }

    // Перевірка, чи є шашка королевою
    public boolean isKing() {
        return type == Type.KING;
    }

    // Getter для статусу життя
    public boolean isAlive() {
        return isAlive;
    }

    // Вбити шашку
    public void kill() {
        isAlive = false;
    }

    // Відродити шашку (якщо потрібно)
    public void resurrect() {
        isAlive = true;
    }
}
package com.example.checkers.model;

public abstract class Player {
    private String name;
    private Piece.Color color;
    private int capturedPieces;

    // Конструктор
    public Player(String name, Piece.Color color) {
        this.name = name;
        this.color = color;
        this.capturedPieces = 0;
    }

    // Абстрактний метод для здійснення ходу
    public abstract boolean makeMove(Board board, int fromRow, int fromCol,
                                     int toRow, int toCol);

    // Getter для імені
    public String getName() {
        return name;
    }

    // Getter для кольору
    public Piece.Color getColor() {
        return color;
    }

    // Додати захоплену шашку
    public void incrementCapturedPieces() {
        capturedPieces++;
    }

    // Отримати кількість захоплених шашок
    public int getCapturedPieces() {
        return capturedPieces;
    }

    // Перевірка, чи може гравець зробити хід
    public boolean canMakeMove(Board board) {
        // Логіка перевірки наявності можливих ходів
        // Реалізація залежатиме від конкретних правил гри
        return board.hasPiecesForColor(color);
    }
}
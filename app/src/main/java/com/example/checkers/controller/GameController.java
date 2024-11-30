package com.example.checkers.controller;

import com.example.checkers.model.Board;
import com.example.checkers.model.Piece;
import com.example.checkers.model.Player;
import com.example.checkers.utils.GameRules;
import com.example.checkers.utils.MoveValidator;
import com.example.checkers.ai.ComputerPlayer;

public class GameController {
    private Board board;
    private Player humanPlayer;
    private Player computerPlayer;
    private Player currentPlayer;
    private GameState gameState;

    // Enum для станів гри
    public enum GameState {
        NOT_STARTED,
        IN_PROGRESS,
        HUMAN_WON,
        COMPUTER_WON,
        DRAW
    }

    // Конструктор
    public GameController(Player humanPlayer, ComputerPlayer computerPlayer) {
        this.board = new Board();
        this.humanPlayer = humanPlayer;
        this.computerPlayer = computerPlayer;
        this.currentPlayer = humanPlayer; // Людина завжди ходить першою
        this.gameState = GameState.NOT_STARTED;
    }

    // Запуск гри
    public void startGame() {
        board = new Board(); // Скидаємо дошку
        currentPlayer = humanPlayer;
        gameState = GameState.IN_PROGRESS;
    }

    // Обробка ходу гравця
    public boolean processPlayerMove(int fromRow, int fromCol, int toRow, int toCol) {
        // Перевірка, чи гра в процесі
        if (gameState != GameState.IN_PROGRESS) {
            return false;
        }

        // Перевірка валідності ходу
        if (!MoveValidator.isValidMove(board, currentPlayer, fromRow, fromCol, toRow, toCol)) {
            return false;
        }

        // Виконання ходу
        boolean moveSuccess = currentPlayer.makeMove(board, fromRow, fromCol, toRow, toCol);

        if (moveSuccess) {
            // Перевірка перемоги
            updateGameState();

            // Зміна поточного гравця
            switchPlayer();

            return true;
        }

        return false;
    }

    // Хід комп'ютера
    public void processComputerMove() {
        // Логіка ходу комп'ютера
        if (currentPlayer == computerPlayer && gameState == GameState.IN_PROGRESS) {
            // Викликаємо AI стратегію комп'ютера
            boolean moveSuccess = computerPlayer.makeMove(board, 0, 0, 0, 0);

            if (moveSuccess) {
                updateGameState();
                switchPlayer();
            }
        }
    }

    // Оновлення стану гри
    private void updateGameState() {
        // Перевірка умов перемоги або нічиєї
        if (!board.hasPiecesForColor(Piece.Color.WHITE)) {
            gameState = GameState.COMPUTER_WON;
        } else if (!board.hasPiecesForColor(Piece.Color.BLACK)) {
            gameState = GameState.HUMAN_WON;
        } else if (!humanPlayer.canMakeMove(board)) {
            gameState = GameState.COMPUTER_WON;
        } else if (!computerPlayer.canMakeMove(board)) {
            gameState = GameState.HUMAN_WON;
        }

        // Додаткова перевірка на нічию (наприклад, за кількістю ходів)
        if (GameRules.isDrawCondition(board)) {
            gameState = GameState.DRAW;
        }
    }

    // Зміна поточного гравця
    private void switchPlayer() {
        currentPlayer = (currentPlayer == humanPlayer)
                ? computerPlayer
                : humanPlayer;
    }

    // Getter для поточного стану гри
    public GameState getGameState() {
        return gameState;
    }

    // Getter для поточного гравця
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    // Getter для ігрової дошки
    public Board getBoard() {
        return board;
    }

    // Завершення гри
    public void endGame() {
        gameState = GameState.NOT_STARTED;
        board = new Board();
    }

    // Отримати статистику гри
    public GameStatistics getGameStatistics() {
        return new GameStatistics(
                humanPlayer.getCapturedPieces(),
                computerPlayer.getCapturedPieces(),
                gameState
        );
    }

    // Внутрішній клас для статистики гри
    public static class GameStatistics {
        private int humanCapturedPieces;
        private int computerCapturedPieces;
        private GameState finalGameState;

        public GameStatistics(int humanCapturedPieces,
                              int computerCapturedPieces,
                              GameState finalGameState) {
            this.humanCapturedPieces = humanCapturedPieces;
            this.computerCapturedPieces = computerCapturedPieces;
            this.finalGameState = finalGameState;
        }

        // Getters
        public int getHumanCapturedPieces() {
            return humanCapturedPieces;
        }

        public int getComputerCapturedPieces() {
            return computerCapturedPieces;
        }

        public GameState getFinalGameState() {
            return finalGameState;
        }
    }
}
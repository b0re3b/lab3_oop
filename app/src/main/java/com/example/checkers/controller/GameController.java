package com.example.checkers.controller;

public class GameController {
    private MoveValidator moveValidator;
    private int currentPlayer;
    private boolean gameOver;
    private int boardSize;

    // Слухачі для подій гри
    private GameStateListener gameStateListener;
    private MoveListener moveListener;

    public interface GameStateListener {
        void onGameWin(int player);
        void onGameDraw();
    }

    public interface MoveListener {
        void onMoveCompleted(int row, int col, int player);
    }

    public GameController(int boardSize) {
        this.boardSize = boardSize;
        this.moveValidator = new MoveValidator(boardSize);
        this.currentPlayer = 1; // Перший гравець починає
        this.gameOver = false;
    }

    /**
     * Встановлення слухача стану гри
     * @param listener слухач стану гри
     */
    public void setGameStateListener(GameStateListener listener) {
        this.gameStateListener = listener;
    }

    /**
     * Встановлення слухача ходів
     * @param listener слухач ходів
     */
    public void setMoveListener(MoveListener listener) {
        this.moveListener = listener;
    }

    /**
     * Здійснення ходу
     * @param row рядок ходу
     * @param col стовпець ходу
     * @return true, якщо хід успішний
     */
    public boolean makeMove(int row, int col) {
        if (gameOver) {
            return false;
        }

        if (moveValidator.makeMove(row, col, currentPlayer)) {
            // Виклик слухача ходу
            if (moveListener != null) {
                moveListener.onMoveCompleted(row, col, currentPlayer);
            }

            // Перевірка перемоги
            if (moveValidator.checkWin(currentPlayer)) {
                gameOver = true;
                if (gameStateListener != null) {
                    gameStateListener.onGameWin(currentPlayer);
                }
                return true;
            }

            // Перевірка нічиєї
            if (moveValidator.checkDraw()) {
                gameOver = true;
                if (gameStateListener != null) {
                    gameStateListener.onGameDraw();
                }
                return true;
            }

            // Зміна гравця
            currentPlayer = (currentPlayer == 1) ? 2 : 1;

            return true;
        }
        return false;
    }

    /**
     * Скидання гри
     */
    public void resetGame() {
        moveValidator.resetBoard();
        currentPlayer = 1;
        gameOver = false;
    }

    /**
     * Отримання поточного гравця
     * @return номер поточного гравця
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Перевірка, чи гра закінчена
     * @return true, якщо гра завершена
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Отримання копії поточного стану дошки
     * @return копія ігрової дошки
     */
    public int[][] getCurrentBoardState() {
        return moveValidator.getBoard();
    }
}
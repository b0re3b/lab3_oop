package com.example.checkers.utils;

public class MoveValidator {
    private int[][] gameBoard;
    private int boardSize;

    public MoveValidator(int boardSize) {
        this.boardSize = boardSize;
        this.gameBoard = new int[boardSize][boardSize];
    }

    /**
     * Перевіряє, чи є хід допустимим
     * @param row рядок ходу
     * @param col стовпець ходу
     * @return true, якщо хід допустимий, інакше false
     */
    public boolean isMoveValid(int row, int col) {
        // Перевірка меж дошки
        if (row < 0 || row >= boardSize || col < 0 || col >= boardSize) {
            return false;
        }

        // Перевірка, чи клітинка вже зайнята
        return gameBoard[row][col] == 0;
    }

    /**
     * Здійснює хід на дошці
     * @param row рядок ходу
     * @param col стовпець ходу
     * @param player гравець (1 або 2)
     * @return true, якщо хід успішний
     */
    public boolean makeMove(int row, int col, int player) {
        if (isMoveValid(row, col)) {
            gameBoard[row][col] = player;
            return true;
        }
        return false;
    }

    /**
     * Перевіряє перемогу гравця
     * @param player гравець (1 або 2)
     * @return true, якщо гравець переміг
     */
    public boolean checkWin(int player) {
        // Перевірка рядків
        for (int row = 0; row < boardSize; row++) {
            int count = 0;
            for (int col = 0; col < boardSize; col++) {
                if (gameBoard[row][col] == player) {
                    count++;
                    if (count == 5) return true;
                } else {
                    count = 0;
                }
            }
        }

        // Перевірка стовпців
        for (int col = 0; col < boardSize; col++) {
            int count = 0;
            for (int row = 0; row < boardSize; row++) {
                if (gameBoard[row][col] == player) {
                    count++;
                    if (count == 5) return true;
                } else {
                    count = 0;
                }
            }
        }

        // Перевірка діагоналей зліва направо
        for (int k = 0; k < 2 * boardSize - 1; k++) {
            int count = 0;
            for (int row = 0; row < boardSize; row++) {
                int col = k - row;
                if (col >= 0 && col < boardSize) {
                    if (gameBoard[row][col] == player) {
                        count++;
                        if (count == 5) return true;
                    } else {
                        count = 0;
                    }
                }
            }
        }

        // Перевірка діагоналей справа наліво
        for (int k = 0; k < 2 * boardSize - 1; k++) {
            int count = 0;
            for (int row = 0; row < boardSize; row++) {
                int col = boardSize - 1 - (k - row);
                if (col >= 0 && col < boardSize) {
                    if (gameBoard[row][col] == player) {
                        count++;
                        if (count == 5) return true;
                    } else {
                        count = 0;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Перевіряє нічию
     * @return true, якщо дошка заповнена
     */
    public boolean checkDraw() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (gameBoard[row][col] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Скидає дошку до початкового стану
     */
    public void resetBoard() {
        gameBoard = new int[boardSize][boardSize];
    }

    /**
     * Отримує поточний стан дошки
     * @return копія ігрової дошки
     */
    public int[][] getBoard() {
        int[][] boardCopy = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            System.arraycopy(gameBoard[i], 0, boardCopy[i], 0, boardSize);
        }
        return boardCopy;
    }
}
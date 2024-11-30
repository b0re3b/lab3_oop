package com.example.checkers.ai;

import com.example.checkers.model.Board;
import com.example.checkers.model.Piece;
import com.example.checkers.utils.GameRules;

import java.util.List;

/**
 * Advanced interface for AI strategies in Checkers.
 * Provides methods for evaluating board state and finding optimal moves.
 */
public interface AIStrategy {
    /**
     * Difficulty levels for the AI strategy.
     */
    enum Difficulty {
        EASY,       // Random valid moves
        MEDIUM,     // Basic strategic considerations
        HARD        // Advanced look-ahead and evaluation
    }

    /**
     * Represents a potential move with additional evaluation information.
     */
    class ScoredMove implements Comparable<ScoredMove> {
        public final Piece piece;
        public final int newRow;
        public final int newCol;
        public final double score;
        public final boolean isCapture;

        public ScoredMove(Piece piece, int newRow, int newCol, double score, boolean isCapture) {
            this.piece = piece;
            this.newRow = newRow;
            this.newCol = newCol;
            this.score = score;
            this.isCapture = isCapture;
        }

        @Override
        public int compareTo(ScoredMove other) {
            return Double.compare(this.score, other.score);
        }
    }

    /**
     * Find the best move for the AI player based on the current board state.
     *
     * @param board Current game board
     * @param playerColor Color of the AI player
     * @return The best move found by the AI strategy
     */
    ScoredMove findBestMove(Board board, Piece.Color playerColor);

    /**
     * Evaluate the current board state from the perspective of the AI player.
     *
     * @param board Current game board
     * @param playerColor Color of the AI player
     * @return A numerical score representing the board's favorability
     */
    double evaluateBoardState(Board board, Piece.Color playerColor);

    /**
     * Get the difficulty level of the current AI strategy.
     *
     * @return The difficulty level of the AI
     */
    Difficulty getDifficulty();

    /**
     * Set the difficulty level for the AI strategy.
     *
     * @param difficulty New difficulty level
     */
    void setDifficulty(Difficulty difficulty);
}
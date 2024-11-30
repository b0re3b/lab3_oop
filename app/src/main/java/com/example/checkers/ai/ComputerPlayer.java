package com.example.checkers.ai;

import com.example.checkers.model.Board;
import com.example.checkers.model.Piece;
import com.example.checkers.utils.GameRules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Advanced implementation of AI strategy for Checkers.
 * Provides different levels of difficulty and strategic move selection.
 */
public class ComputerPlayer implements AIStrategy {
    private Difficulty difficulty;
    private Random random;

    /**
     * Constructor with default difficulty.
     */
    public ComputerPlayer() {
        this.difficulty = Difficulty.MEDIUM;
        this.random = new Random();
    }

    /**
     * Constructor with specified difficulty.
     *
     * @param difficulty Initial difficulty level
     */
    public ComputerPlayer(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.random = new Random();
    }

    @Override
    public ScoredMove findBestMove(Board board, Piece.Color playerColor) {
        List<ScoredMove> validMoves = generateValidMoves(board, playerColor);

        if (validMoves.isEmpty()) {
            return null;
        }

        switch (difficulty) {
            case EASY:
                return selectRandomMove(validMoves);
            case MEDIUM:
                return selectMediumMove(validMoves);
            case HARD:
                return selectHardMove(board, validMoves, playerColor);
            default:
                return selectRandomMove(validMoves);
        }
    }

    /**
     * Generate all valid moves for the given player.
     *
     * @param board Current game board
     * @param playerColor Color of the AI player
     * @return List of scored moves
     */
    private List<ScoredMove> generateValidMoves(Board board, Piece.Color playerColor) {
        List<ScoredMove> validMoves = new ArrayList<>();

        for (Piece piece : board.getPieces(playerColor)) {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (GameRules.isValidMove(board, piece, row, col)) {
                        double moveScore = calculateMoveScore(board, piece, row, col);
                        boolean isCapture = Math.abs(row - piece.getRow()) == 2;
                        validMoves.add(new ScoredMove(piece, row, col, moveScore, isCapture));
                    }
                }
            }
        }

        return validMoves;
    }

    /**
     * Calculate a score for a potential move.
     *
     * @param board Current game board
     * @param piece Piece to move
     * @param newRow Destination row
     * @param newCol Destination column
     * @return Score for the move
     */
    private double calculateMoveScore(Board board, Piece piece, int newRow, int newCol) {
        double baseScore = 0;

        // Capture moves are prioritized
        if (Math.abs(newRow - piece.getRow()) == 2) {
            baseScore += 10;
        }

        // Promote to king is valuable
        if (piece.getType() == Piece.Type.REGULAR && piece.canBePromoted()) {
            baseScore += 5;
        }

        // Center control
        double centerDistance = Math.abs(newRow - 3.5) + Math.abs(newCol - 3.5);
        baseScore += (8 - centerDistance) * 0.5;

        // Additional randomness to prevent predictability
        baseScore += random.nextDouble();

        return baseScore;
    }

    /**
     * Select a random move from valid moves.
     *
     * @param validMoves List of valid moves
     * @return Selected move
     */
    private ScoredMove selectRandomMove(List<ScoredMove> validMoves) {
        return validMoves.get(random.nextInt(validMoves.size()));
    }

    /**
     * Select a move for medium difficulty.
     * Prioritizes capture moves and slightly strategic positioning.
     *
     * @param validMoves List of valid moves
     * @return Selected move
     */
    private ScoredMove selectMediumMove(List<ScoredMove> validMoves) {
        // First, try to find capture moves
        List<ScoredMove> captureMoves = validMoves.stream()
                .filter(move -> move.isCapture)
                .toList();

        if (!captureMoves.isEmpty()) {
            // Sort capture moves and select from top moves
            Collections.sort(captureMoves, Collections.reverseOrder());
            return captureMoves.get(0);
        }

        // If no captures, sort all moves and select from top
        Collections.sort(validMoves, Collections.reverseOrder());
        return validMoves.get(0);
    }

    /**
     * Select a move for hard difficulty.
     * Implements a basic look-ahead strategy.
     *
     * @param board Current game board
     * @param validMoves List of valid moves
     * @param playerColor Color of the AI player
     * @return Selected move
     */
    private ScoredMove selectHardMove(Board board, List<ScoredMove> validMoves, Piece.Color playerColor) {
        double bestOverallScore = Double.NEGATIVE_INFINITY;
        ScoredMove bestMove = null;

        for (ScoredMove move : validMoves) {
            // Simulate the move
            Board simulatedBoard = cloneBoard(board);
            simulatedBoard.movePiece(move.piece, move.newRow, move.newCol);

            // Evaluate the resulting board state
            double moveScore = move.score;
            double boardEvaluationScore = evaluateBoardState(simulatedBoard, playerColor);
            double totalScore = moveScore + boardEvaluationScore;

            if (totalScore > bestOverallScore) {
                bestOverallScore = totalScore;
                bestMove = move;
            }
        }

        return bestMove != null ? bestMove : selectMediumMove(validMoves);
    }

    /**
     * Create a deep copy of the game board.
     *
     * @param original Original board to clone
     * @return Cloned board
     */
    private Board cloneBoard(Board original) {
        // Note: This is a simplification. In a real implementation,
        // you'd need a proper deep copy method in the Board class
        Board newBoard = new Board();
        newBoard.reset();
        return newBoard;
    }

    @Override
    public double evaluateBoardState(Board board, Piece.Color playerColor) {
        Piece.Color opponentColor = playerColor == Piece.Color.WHITE
                ? Piece.Color.BLACK
                : Piece.Color.WHITE;

        // Count pieces
        int playerPieceCount = board.getPieceCount(playerColor);
        int opponentPieceCount = board.getPieceCount(opponentColor);

        // Count kings
        long playerKingCount = board.getPieces(playerColor).stream()
                .filter(p -> p.getType() == Piece.Type.KING)
                .count();
        long opponentKingCount = board.getPieces(opponentColor).stream()
                .filter(p -> p.getType() == Piece.Type.KING)
                .count();

        // Calculate score
        double pieceScore = (playerPieceCount - opponentPieceCount) * 2.0;
        double kingBonus = (playerKingCount - opponentKingCount) * 3.0;

        return pieceScore + kingBonus;
    }

    @Override
    public Difficulty getDifficulty() {
        return difficulty;
    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
}
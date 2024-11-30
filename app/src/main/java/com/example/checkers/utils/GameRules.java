package com.example.checkers.utils;

import com.example.checkers.model.Board;
import com.example.checkers.model.Piece;

/**
 * Defines the rules and validation logic for the Checkers game.
 */
public class GameRules {
    /**
     * Check if a move is valid according to Checkers rules.
     *
     * @param board The current game board
     * @param piece The piece to be moved
     * @param newRow Destination row
     * @param newCol Destination column
     * @return true if the move is valid, false otherwise
     */
    public static boolean isValidMove(Board board, Piece piece, int newRow, int newCol) {
        // Check if destination is within board
        if (!board.isValidPosition(newRow, newCol)) {
            return false;
        }

        // Check if destination is empty
        if (board.getPieceAt(newRow, newCol) != null) {
            return false;
        }

        // Calculate move distance and direction
        int rowDiff = Math.abs(newRow - piece.getRow());
        int colDiff = Math.abs(newCol - piece.getCol());

        // Regular piece movement
        if (piece.getType() == Piece.Type.REGULAR) {
            // Determine allowed moving direction based on piece color
            int allowedDirection = piece.getColor() == Piece.Color.WHITE ? -1 : 1;

            // Simple diagonal move
            if (rowDiff == 1 && colDiff == 1 &&
                    (newRow - piece.getRow()) == allowedDirection) {
                return true;
            }

            // Capture move
            if (rowDiff == 2 && colDiff == 2 &&
                    (newRow - piece.getRow()) == (2 * allowedDirection)) {
                // Check for opponent piece to capture
                int capturedRow = (piece.getRow() + newRow) / 2;
                int capturedCol = (piece.getCol() + newCol) / 2;

                Piece capturedPiece = board.getPieceAt(capturedRow, capturedCol);
                return capturedPiece != null &&
                        capturedPiece.getColor() != piece.getColor();
            }
        }

        // King piece movement (can move in any diagonal direction)
        if (piece.getType() == Piece.Type.KING) {
            // Simple diagonal move
            if (rowDiff == 1 && colDiff == 1) {
                return true;
            }

            // Capture move
            if (rowDiff == 2 && colDiff == 2) {
                // Check for opponent piece to capture
                int capturedRow = (piece.getRow() + newRow) / 2;
                int capturedCol = (piece.getCol() + newCol) / 2;

                Piece capturedPiece = board.getPieceAt(capturedRow, capturedCol);
                return capturedPiece != null &&
                        capturedPiece.getColor() != piece.getColor();
            }
        }

        return false;
    }

    /**
     * Check if a player has won the game.
     *
     * @param board The current game board
     * @param playerColor Color of the player to check
     * @return true if the player has won, false otherwise
     */
    public static boolean hasWon(Board board, Piece.Color playerColor) {
        Piece.Color opponentColor = playerColor == Piece.Color.WHITE
                ? Piece.Color.BLACK
                : Piece.Color.WHITE;

        // Player wins if opponent has no pieces or cannot make a move
        return board.getPieceCount(opponentColor) == 0 ||
                !hasValidMoves(board, opponentColor);
    }

    /**
     * Check if a player has any valid moves left.
     *
     * @param board The current game board
     * @param playerColor Color of the player to check
     * @return true if the player has valid moves, false otherwise
     */
    public static boolean hasValidMoves(Board board, Piece.Color playerColor) {
        for (Piece piece : board.getPieces(playerColor)) {
            // Check all possible moves for the piece
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (isValidMove(board, piece, row, col)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
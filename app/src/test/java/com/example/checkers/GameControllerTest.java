package com.example.checkers;

import com.example.checkers.controller.GameController;
import com.example.checkers.model.Board;
import com.example.checkers.model.Piece;
import com.example.checkers.model.Player;
import com.example.checkers.ai.ComputerPlayer;
import com.example.checkers.utils.GameRules;
import com.example.checkers.utils.MoveValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameControllerTest {

    private Board board;
    private Player humanPlayer;
    private ComputerPlayer computerPlayer;
    private GameController gameController;

    @BeforeEach
    void setUp() {
        humanPlayer = mock(Player.class);
        computerPlayer = mock(ComputerPlayer.class);
        board = mock(Board.class);

        when(humanPlayer.getColor()).thenReturn(Piece.Color.WHITE);
        when(computerPlayer.getColor()).thenReturn(Piece.Color.BLACK);

        gameController = new GameController(humanPlayer, computerPlayer);
    }

    @Test
    void testStartGame() {
        gameController.startGame();

        assertEquals(GameController.GameState.IN_PROGRESS, gameController.getGameState());
        assertEquals(humanPlayer, gameController.getCurrentPlayer());
    }

    @Test
    void testProcessPlayerMoveValidMove() {
        int fromRow = 2, fromCol = 2, toRow = 3, toCol = 3;

        when(MoveValidator.isValidMove(board, humanPlayer, fromRow, fromCol, toRow, toCol)).thenReturn(true);
        when(humanPlayer.makeMove(board, fromRow, fromCol, toRow, toCol)).thenReturn(true);

        boolean moveResult = gameController.processPlayerMove(fromRow, fromCol, toRow, toCol);

        assertTrue(moveResult);
        verify(humanPlayer, times(1)).makeMove(board, fromRow, fromCol, toRow, toCol);
    }

    @Test
    void testProcessPlayerMoveInvalidMove() {
        int fromRow = 2, fromCol = 2, toRow = 3, toCol = 3;

        when(MoveValidator.isValidMove(board, humanPlayer, fromRow, fromCol, toRow, toCol)).thenReturn(false);

        boolean moveResult = gameController.processPlayerMove(fromRow, fromCol, toRow, toCol);

        assertFalse(moveResult);
    }

    @Test
    void testProcessComputerMove() {
        when(computerPlayer.makeMove(board, 0, 0, 0, 0)).thenReturn(true);

        gameController.processComputerMove();

        verify(computerPlayer, times(1)).makeMove(board, 0, 0, 0, 0);
        assertEquals(humanPlayer, gameController.getCurrentPlayer()); // After computer move, it's human's turn
    }

    @Test
    void testUpdateGameStateHumanWins() {
        when(board.hasPiecesForColor(Piece.Color.WHITE)).thenReturn(false);
        when(board.hasPiecesForColor(Piece.Color.BLACK)).thenReturn(true);

        gameController.startGame();
        gameController.processPlayerMove(2, 2, 3, 3);

        assertEquals(GameController.GameState.HUMAN_WON, gameController.getGameState());
    }

    @Test
    void testUpdateGameStateComputerWins() {
        when(board.hasPiecesForColor(Piece.Color.WHITE)).thenReturn(true);
        when(board.hasPiecesForColor(Piece.Color.BLACK)).thenReturn(false);

        gameController.startGame();
        gameController.processPlayerMove(2, 2, 3, 3);

        assertEquals(GameController.GameState.COMPUTER_WON, gameController.getGameState());
    }

    @Test
    void testDrawCondition() {
        when(GameRules.isDrawCondition(board)).thenReturn(true);

        gameController.startGame();
        gameController.processPlayerMove(2, 2, 3, 3);

        assertEquals(GameController.GameState.DRAW, gameController.getGameState());
    }

    @Test
    void testSwitchPlayer() {
        gameController.startGame();

        Player currentPlayerBeforeSwitch = gameController.getCurrentPlayer();
        gameController.processPlayerMove(2, 2, 3, 3); // After player's move, turn should switch
        Player currentPlayerAfterSwitch = gameController.getCurrentPlayer();

        assertNotEquals(currentPlayerBeforeSwitch, currentPlayerAfterSwitch);
    }

    @Test
    void testEndGame() {
        gameController.startGame();
        gameController.endGame();

        assertEquals(GameController.GameState.NOT_STARTED, gameController.getGameState());
        assertNull(gameController.getBoard()); // Board is reset
    }

    @Test
    void testGameStatistics() {
        when(humanPlayer.getCapturedPieces()).thenReturn(2);
        when(computerPlayer.getCapturedPieces()).thenReturn(3);
        gameController.startGame();

        GameController.GameStatistics stats = gameController.getGameStatistics();

        assertEquals(2, stats.getHumanCapturedPieces());
        assertEquals(3, stats.getComputerCapturedPieces());
        assertEquals(GameController.GameState.IN_PROGRESS, stats.getFinalGameState());
    }
}

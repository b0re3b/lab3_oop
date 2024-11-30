package com.example.checkers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import com.example.checkers.ai.AIStrategy;
import com.example.checkers.ai.BasicStrategy;
import com.example.checkers.ai.ComputerPlayer;
import com.example.checkers.model.Board;
import com.example.checkers.model.Piece;
import com.example.checkers.utils.MoveValidator;

import static org.junit.jupiter.api.Assertions.*;

class ComputerPlayerTest {

    private Board board;
    private ComputerPlayer computerPlayer;
    private BasicStrategy strategy;

    @BeforeEach
    void setUp() {
        // Створюємо необхідні об'єкти для тесту
        strategy = mock(BasicStrategy.class);
        board = mock(Board.class);
        computerPlayer = new ComputerPlayer("AI", Piece.Color.WHITE, strategy);
    }

    @Test
    void testMakeMoveWhenValidMoveIsAvailable() {
        // Створюємо фіктивний хід
        AIStrategy.Move mockMove = new AIStrategy.Move(2, 2, 3, 3);

        // Налаштовуємо поведінку mock для BasicStrategy
        when(strategy.chooseMove(board, Piece.Color.WHITE)).thenReturn(mockMove);

        // Налаштовуємо, щоб дошка дозволяла цей хід
        when(MoveValidator.isValidMove(board, computerPlayer, 2, 2, 3, 3)).thenReturn(true);

        // Виконуємо хід
        boolean result = computerPlayer.makeMove(board, 2, 2, 3, 3);

        // Перевірка, що хід був виконаний
        assertTrue(result);
        verify(board, times(1)).removePieceAt(2, 2);
        verify(board, times(1)).setPieceAt(3, 3, any(Piece.class));
    }

    @Test
    void testMakeMoveWhenInvalidMoveIsChosen() {
        // Створюємо фіктивний хід
        AIStrategy.Move mockMove = new AIStrategy.Move(2, 2, 3, 3);

        // Налаштовуємо mock для стратегії
        when(strategy.chooseMove(board, Piece.Color.WHITE)).thenReturn(mockMove);

        // Налаштовуємо, щоб MoveValidator повертав false для цього ходу
        when(MoveValidator.isValidMove(board, computerPlayer, 2, 2, 3, 3)).thenReturn(false);

        // Виконуємо хід
        boolean result = computerPlayer.makeMove(board, 2, 2, 3, 3);

        // Перевірка, що хід не був виконаний
        assertFalse(result);
        verify(board, never()).removePieceAt(anyInt(), anyInt());
        verify(board, never()).setPieceAt(anyInt(), anyInt(), any(Piece.class));
    }

    @Test
    void testCaptureEnemyPiece() {
        // Створюємо фіктивний хід, де шашка суперника буде захоплена
        AIStrategy.Move mockMove = new AIStrategy.Move(2, 2, 4, 4);

        Piece capturedPiece = new Piece(Piece.Color.BLACK);
        when(board.getPieceAt(3, 3)).thenReturn(capturedPiece); // Між ними знаходиться шашка суперника

        // Налаштовуємо mock для стратегії
        when(strategy.chooseMove(board, Piece.Color.WHITE)).thenReturn(mockMove);
        when(MoveValidator.isValidMove(board, computerPlayer, 2, 2, 4, 4)).thenReturn(true);

        // Виконуємо хід
        computerPlayer.makeMove(board, 2, 2, 4, 4);

        // Перевіряємо, чи шашка була захоплена
        verify(board, times(1)).removePieceAt(3, 3);
        assertEquals(1, computerPlayer.getCapturedPieces()); // Припустимо, метод для підрахунку захоплених шашок
    }

    @Test
    void testNoPossibleMove() {
        // Налаштовуємо ситуацію, де немає можливих ходів
        when(strategy.chooseMove(board, Piece.Color.WHITE)).thenReturn(null);

        // Виконуємо хід
        boolean result = computerPlayer.makeMove(board, 2, 2, 3, 3);

        // Перевірка, що хід не був виконаний
        assertFalse(result);
    }
}

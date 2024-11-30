package com.example.checkers;

import com.example.checkers.ai.AIStrategy;
import com.example.checkers.ai.BasicStrategy;
import com.example.checkers.model.Board;
import com.example.checkers.model.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicStrategyTest {

    private BasicStrategy basicStrategy;
    private Board board;

    @BeforeEach
    public void setUp() {
        basicStrategy = new BasicStrategy();
        board = new Board(); // Припускаємо, що дошка ініціалізована зі стандартними фігурами
    }

    @Test
    public void testChooseMoveWhenNoPossibleMoves() {
        // У разі, якщо немає доступних ходів (наприклад, дошка заблокована), метод має повернути null
        Piece.Color aiColor = Piece.Color.WHITE;

        // Можна припустити, що у такому випадку немає ходів
        AIStrategy.Move move = basicStrategy.chooseMove(board, aiColor);
        assertNull(move, "Expected null when there are no possible moves.");
    }

    @Test
    public void testChooseMoveWhenPossibleMovesExist() {
        // Перевіряємо, чи метод правильно вибирає хід, коли є доступні ходи для AI
        Piece.Color aiColor = Piece.Color.WHITE;

        // Оскільки ми використовуємо стандартний початковий стан, перевіряємо, що хід існує
        AIStrategy.Move move = basicStrategy.chooseMove(board, aiColor);
        assertNotNull(move, "Expected a move to be chosen when possible moves exist.");
    }

    @Test
    public void testChooseMoveRandomness() {
        // Перевіряємо, чи вибір ходу відбувається випадковим чином, якщо є кілька можливих ходів
        Piece.Color aiColor = Piece.Color.WHITE;

        // Оскільки на початковій дошці кілька можливих ходів, перевіримо випадковість вибору
        AIStrategy.Move firstMove = basicStrategy.chooseMove(board, aiColor);
        AIStrategy.Move secondMove = basicStrategy.chooseMove(board, aiColor);

        // Перевіряємо, чи вибір змінюється
        assertNotEquals(firstMove, secondMove, "Expected a different move on subsequent calls.");
    }
}

package com.example.checkers;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.checkers.model.Board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.checkers.ai.ComputerPlayer;
import com.example.checkers.ai.BasicStrategy;
import com.example.checkers.controller.GameController;
import com.example.checkers.model.Piece;
import com.example.checkers.model.Player;
import com.example.checkers.view.BoardView;

public class MainActivity extends AppCompatActivity {
    private GameController gameController;
    private BoardView boardView;
    private TextView gameStatusText;
    private Button newGameButton;
    private Button surrenderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ініціалізація UI компонентів
        initializeUIComponents();

        // Створення гравців
        Player humanPlayer = createHumanPlayer();
        Player computerPlayer = createComputerPlayer();

        // Створення контролера гри
        gameController = new GameController(humanPlayer, (ComputerPlayer) computerPlayer);

        // Налаштування BoardView
        boardView.setGameController(gameController);
        boardView.setOnMoveListener(this::handlePlayerMove);

        // Додавання слухачів подій для кнопок
        setupButtonListeners();

        // Старт гри
        startNewGame();
    }

    // Ініціалізація UI компонентів
    private void initializeUIComponents() {
        setContentView(R.layout.activity_main);
        boardView = findViewById(R.id.board_view);
        gameStatusText = findViewById(R.id.game_status_text);
        newGameButton = findViewById(R.id.new_game_button);
        surrenderButton = findViewById(R.id.surrender_button);
    }

    // Створення гравця-людини
    private Player createHumanPlayer() {
        return new Player("Гравець", Piece.Color.BLACK) {
            @Override
            public boolean makeMove(Board board, int fromRow, int fromCol, int toRow, int toCol) {
                // Реалізація ходу гравця-людини
                return true;
            }
        };
    }

    // Створення комп'ютерного гравця
    private Player createComputerPlayer() {
        BasicStrategy basicStrategy = new BasicStrategy();
        return new ComputerPlayer("Комп'ютер", Piece.Color.WHITE, basicStrategy);
    }


    // Обробка ходу гравця
    private void handlePlayerMove(int fromRow, int fromCol, int toRow, int toCol) {
        boolean moveSuccessful = gameController.processPlayerMove(fromRow, fromCol, toRow, toCol);

        if (moveSuccessful) {
            updateGameStatus();

            // Хід комп'ютера
            gameController.processComputerMove();

            updateGameStatus();
        } else {
            Toast.makeText(this, "Невірний хід", Toast.LENGTH_SHORT).show();
        }
    }

    // Налаштування слухачів для кнопок
    private void setupButtonListeners() {
        newGameButton.setOnClickListener(v -> startNewGame());

        surrenderButton.setOnClickListener(v -> {
            gameController.endGame();
            Toast.makeText(this, "Ви здалися!", Toast.LENGTH_SHORT).show();
            updateGameStatus();
        });
    }

    // Початок нової гри
    private void startNewGame() {
        gameController.startGame();
        boardView.resetBoard();
        updateGameStatus();
    }

    // Оновлення статусу гри
    @SuppressLint("SetTextI18n")
    private void updateGameStatus() {
        switch (gameController.getGameState()) {
            case IN_PROGRESS:
                String currentPlayer = gameController.getCurrentPlayer() instanceof ComputerPlayer
                        ? "Комп'ютер"
                        : "Гравець";
                gameStatusText.setText("Хід: " + currentPlayer);
                break;
            case HUMAN_WON:
                gameStatusText.setText("Ви перемогли!");
                break;
            case COMPUTER_WON:
                gameStatusText.setText("Комп'ютер переміг!");
                break;
            case DRAW:
                gameStatusText.setText("Нічия!");
                break;
            default:
                gameStatusText.setText("Гра не розпочата");
        }
    }

    // Збереження стану гри при зміні конфігурації
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Можна додати збереження стану гри
    }
}

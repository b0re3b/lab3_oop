import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int BOARD_SIZE = 15;
    private GameController gameController;
    private Button[][] boardButtons;
    private TextView playerTurnTextView;
    private GridLayout gameGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ініціалізація контролера гри
        gameController = new GameController(BOARD_SIZE);

        // Налаштування текстового поля статусу гравця
        playerTurnTextView = findViewById(R.id.player_turn_text_view);

        // Налаштування ігрової сітки
        gameGrid = findViewById(R.id.game_grid);
        gameGrid.setColumnCount(BOARD_SIZE);
        gameGrid.setRowCount(BOARD_SIZE);

        // Створення кнопок для гральної дошки
        boardButtons = new Button[BOARD_SIZE][BOARD_SIZE];
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Button button = createBoardButton(row, col);
                gameGrid.addView(button);
                boardButtons[row][col] = button;
            }
        }

        // Налаштування слухачів
        setupGameListeners();

        // Кнопка скидання гри
        Button resetButton = findViewById(R.id.reset_button);
        resetButton.setOnClickListener(v -> resetGame());

        // Оновлення статусу гравця
        updatePlayerTurnText();
    }

    /**
     * Створення кнопки для клітинки на ігровій дошці
     * @param row рядок кнопки
     * @param col стовпець кнопки
     * @return кнопка для ходу
     */
    private Button createBoardButton(int row, int col) {
        Button button = new Button(this);

        // Налаштування параметрів кнопки
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = 0;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        button.setLayoutParams(params);

        // Додавання обробника кліку
        button.setOnClickListener(v -> onBoardCellClicked(row, col));

        return button;
    }

    /**
     * Обробка кліку на клітинці дошки
     * @param row рядок
     * @param col стовпець
     */
    private void onBoardCellClicked(int row, int col) {
        if (gameController.makeMove(row, col)) {
            // Хід успішний, оновлення UI
            updateBoardUI();
        } else {
            // Показ повідомлення про неправильний хід
            Toast.makeText(this, "Неправильний хід!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Налаштування слухачів контролера гри
     */
    private void setupGameListeners() {
        gameController.setGameStateListener(new GameController.GameStateListener() {
            @Override
            public void onGameWin(int player) {
                // Показ повідомлення про перемогу
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this,
                            "Гравець " + player + " переміг!",
                            Toast.LENGTH_LONG).show();
                    disableBoardButtons();
                });
            }

            @Override
            public void onGameDraw() {
                // Показ повідомлення про нічию
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this,
                            "Нічия!",
                            Toast.LENGTH_LONG).show();
                    disableBoardButtons();
                });
            }
        });

        gameController.setMoveListener((row, col, player) ->
                runOnUiThread(() -> {
                    // Оновлення кнопки після ходу
                    Button button = boardButtons[row][col];
                    button.setText(String.valueOf(player));
                    button.setEnabled(false);
                    updatePlayerTurnText();
                })
        );
    }

    /**
     * Оновлення тексту статусу поточного гравця
     */
    private void updatePlayerTurnText() {
        int currentPlayer = gameController.getCurrentPlayer();
        playerTurnTextView.setText("Хід гравця " + currentPlayer);
    }

    /**
     * Оновлення UI ігрової дошки
     */
    private void updateBoardUI() {
        int[][] boardState = gameController.getCurrentBoardState();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Button button = boardButtons[row][col];
                if (boardState[row][col] != 0) {
                    button.setText(String.valueOf(boardState[row][col]));
                    button.setEnabled(false);
                }
            }
        }
    }

    /**
     * Вимкнення кнопок дошки після закінчення гри
     */
    private void disableBoardButtons() {
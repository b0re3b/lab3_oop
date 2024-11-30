package com.example.checkers.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.checkers.R;
import com.example.checkers.controller.GameController;
import com.example.checkers.model.Board;
import com.example.checkers.model.Piece;

import androidx.annotation.Nullable;

public class BoardView extends View {
    // Board dimensions
    private static final int BOARD_SIZE = 8;

    // Paint objects for drawing
    private Paint boardPaint;
    private Paint darkSquarePaint;
    private Paint lightSquarePaint;
    private Paint piecePaint;
    private Paint selectedPiecePaint;
    private Paint possibleMovePaint;

    // Touch and selection tracking
    private int selectedRow = -1;
    private int selectedCol = -1;

    // Game-related references
    private GameController gameController;
    private Board board;

    // Listener for move events
    private OnMoveListener moveListener;

    // Interface for move events
    public interface OnMoveListener {
        void onMove(int fromRow, int fromCol, int toRow, int toCol);
    }

    // Constructors
    public BoardView(Context context) {
        super(context);
        init();
    }

    public BoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // Initialization method
    private void init() {
        // Initialize paints
        boardPaint = new Paint();
        boardPaint.setColor(Color.BLACK);
        boardPaint.setStyle(Paint.Style.STROKE);

        darkSquarePaint = new Paint();
        darkSquarePaint.setColor(Color.DKGRAY);

        lightSquarePaint = new Paint();
        lightSquarePaint.setColor(Color.LTGRAY);

        piecePaint = new Paint();
        piecePaint.setStyle(Paint.Style.FILL);

        selectedPiecePaint = new Paint();
        selectedPiecePaint.setColor(Color.argb(100, 0, 255, 0));

        possibleMovePaint = new Paint();
        possibleMovePaint.setColor(Color.argb(100, 0, 0, 255));
    }

    // Set game controller
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        this.board = gameController.getBoard();
    }

    // Set move listener
    public void setOnMoveListener(OnMoveListener listener) {
        this.moveListener = listener;
    }

    // Reset the board
    public void resetBoard() {
        selectedRow = -1;
        selectedCol = -1;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Calculate square size
        int squareSize = Math.min(getWidth(), getHeight()) / BOARD_SIZE;

        // Draw board squares
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                // Determine square color
                Paint squarePaint = (row + col) % 2 == 0 ? lightSquarePaint : darkSquarePaint;

                // Draw square
                RectF square = new RectF(
                        col * squareSize,
                        row * squareSize,
                        (col + 1) * squareSize,
                        (row + 1) * squareSize
                );
                canvas.drawRect(square, squarePaint);

                // Highlight selected piece
                if (row == selectedRow && col == selectedCol) {
                    canvas.drawRect(square, selectedPiecePaint);
                }

                // Draw piece
                Piece piece = board.getPieceAt(row, col);
                if (piece != null) {
                    drawPiece(canvas, piece, row, col, squareSize);
                }
            }
        }

        // Draw board border
        canvas.drawRect(0, 0, getWidth(), getHeight(), boardPaint);
    }

    // Draw individual piece
    private void drawPiece(Canvas canvas, Piece piece, int row, int col, int squareSize) {
        // Calculate piece position
        float centerX = col * squareSize + squareSize / 2f;
        float centerY = row * squareSize + squareSize / 2f;
        float radius = squareSize * 0.4f;

        // Set piece color
        piecePaint.setColor(piece.getColor() == Piece.Color.WHITE ? Color.WHITE : Color.BLACK);

        // Draw piece
        canvas.drawCircle(centerX, centerY, radius, piecePaint);

        // Draw king crown if piece is a king
        if (piece.isKing()) {
            drawKingCrown(canvas, centerX, centerY, radius);
        }
    }

    // Draw king crown
    private void drawKingCrown(Canvas canvas, float centerX, float centerY, float radius) {
        Path crownPath = new Path();
        crownPath.moveTo(centerX - radius/2, centerY - radius/3);
        crownPath.lineTo(centerX, centerY - radius);
        crownPath.lineTo(centerX + radius/2, centerY - radius/3);

        Paint crownPaint = new Paint();
        crownPaint.setColor(Color.YELLOW);
        crownPaint.setStyle(Paint.Style.FILL);

        canvas.drawPath(crownPath, crownPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Calculate touched square
            int squareSize = Math.min(getWidth(), getHeight()) / BOARD_SIZE;
            int col = (int) (event.getX() / squareSize);
            int row = (int) (event.getY() / squareSize);

            // Handle piece selection and move
            return handleTouch(row, col);
        }
        return super.onTouchEvent(event);
    }

    // Handle touch events for piece selection and movement
    private boolean handleTouch(int row, int col) {
        // Ensure touch is within board
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return false;
        }

        // No piece currently selected
        if (selectedRow == -1) {
            // Select a piece
            Piece piece = board.getPieceAt(row, col);
            if (piece != null && piece.getColor() == gameController.getCurrentPlayer().getColor()) {
                selectedRow = row;
                selectedCol = col;
                invalidate();
                return true;
            }
        } else {
            // Attempt to move the selected piece
            if (moveListener != null) {
                moveListener.onMove(selectedRow, selectedCol, row, col);

                // Reset selection
                selectedRow = -1;
                selectedCol = -1;
                invalidate();
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Make the view square
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(width, height);

        int squareSizeSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        super.onMeasure(squareSizeSpec, squareSizeSpec);
    }
}
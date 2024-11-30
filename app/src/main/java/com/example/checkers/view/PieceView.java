package com.example.checkers.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.checkers.model.Piece;

/**
 * Custom View for rendering a checkers piece with advanced rendering capabilities.
 */
public class PieceView extends View {
    // Paint objects for drawing
    private Paint piecePaint;
    private Paint highlightPaint;
    private Paint kingMarkPaint;

    // Piece representation
    private Piece piece;

    // Drawing parameters
    private RectF pieceRect;
    private boolean isSelected;
    private boolean isHighlighted;

    /**
     * Constructors for PieceView
     */
    public PieceView(Context context) {
        super(context);
        initializePaints();
    }

    public PieceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializePaints();
    }

    public PieceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializePaints();
    }

    /**
     * Initialize paint objects for piece rendering
     */
    private void initializePaints() {
        // Piece paint
        piecePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        piecePaint.setStyle(Paint.Style.FILL);

        // Highlight paint
        highlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlightPaint.setStyle(Paint.Style.STROKE);
        highlightPaint.setStrokeWidth(5f);
        highlightPaint.setColor(Color.YELLOW);

        // King marker paint
        kingMarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        kingMarkPaint.setStyle(Paint.Style.FILL);
        kingMarkPaint.setColor(Color.GOLD);
    }

    /**
     * Set the piece for this view
     * @param piece The checkers piece to display
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
        updatePieceColor();
        invalidate(); // Trigger redraw
    }

    /**
     * Update piece color based on piece type
     */
    private void updatePieceColor() {
        if (piece != null) {
            piecePaint.setColor(piece.isWhite() ? Color.WHITE : Color.BLACK);
        }
    }

    /**
     * Set selection state
     * @param selected Whether the piece is currently selected
     */
    public void setSelected(boolean selected) {
        this.isSelected = selected;
        invalidate();
    }

    /**
     * Set highlight state
     * @param highlighted Whether the piece should be highlighted
     */
    public void setHighlighted(boolean highlighted) {
        this.isHighlighted = highlighted;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Create a rectangular bound for the piece
        float padding = Math.min(w, h) * 0.1f; // 10% padding
        pieceRect = new RectF(padding, padding, w - padding, h - padding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (piece == null) {
            return; // Don't draw if no piece is set
        }

        // Draw the base piece
        canvas.drawOval(pieceRect, piecePaint);

        // Draw selection highlight if selected
        if (isSelected) {
            canvas.drawOval(pieceRect, highlightPaint);
        }

        // Draw highlight if piece is highlighted
        if (isHighlighted) {
            highlightPaint.setColor(Color.GREEN);
            canvas.drawOval(pieceRect, highlightPaint);
        }

        // Draw king marker if piece is a king
        if (piece.isKing()) {
            // Draw a small crown or marker to indicate king status
            float centerX = pieceRect.centerX();
            float centerY = pieceRect.centerY();
            float radius = Math.min(pieceRect.width(), pieceRect.height()) / 4;
            canvas.drawCircle(centerX, centerY, radius, kingMarkPaint);
        }
    }

    /**
     * Get the current piece
     * @return The current piece
     */
    public Piece getPiece() {
        return piece;
    }
}
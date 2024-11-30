package com.example.checkers.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.checkers.R;
import com.example.checkers.model.Piece;

/**
 * Custom View for rendering a checkers piece with advanced rendering capabilities.
 */
public class PieceView extends View {
    // Paint objects for drawing
    private Paint piecePaint;
    private Paint highlightPaint;

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
        Paint kingMarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        kingMarkPaint.setStyle(Paint.Style.FILL);
        kingMarkPaint.setColor(Color.rgb(255, 215, 0));
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
    protected void onDraw(@NonNull Canvas canvas) {
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
            // Draw a crown image if piece is a king
            @SuppressLint("DrawAllocation") Bitmap crownBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.king_crown);
            float centerX = pieceRect.centerX();
            float centerY = pieceRect.centerY();
            float radius = Math.min(pieceRect.width(), pieceRect.height()) / 4;
            float crownWidth = radius * 2; // Adjust size of the crown
            float crownHeight = crownWidth; // You can change this depending on the aspect ratio of your crown image
            @SuppressLint("DrawAllocation") RectF crownRect = new RectF(centerX - crownWidth / 2, centerY - crownHeight / 2, centerX + crownWidth / 2, centerY + crownHeight / 2);
            canvas.drawBitmap(crownBitmap, null, crownRect, null); // Draw the crown bitmap
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
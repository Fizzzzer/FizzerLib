package com.fizzer.widget.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.Gravity
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import com.fizzer.widget.R


class StrokedTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val strokePaint = TextPaint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private var strokeColor: Int = Color.BLACK
    private var strokeWidth: Float = 6f

    private var strokeLayout: StaticLayout? = null
    private var textLayout: StaticLayout? = null

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.StrokedTextView, 0, 0).apply {
            try {
                strokeColor = getColor(R.styleable.StrokedTextView_stv_strokeColor, Color.BLACK)
                strokeWidth = getDimension(R.styleable.StrokedTextView_stv_strokeThickness, 6f)
            } finally {
                recycle()
            }
        }

        strokePaint.color = strokeColor
        strokePaint.strokeWidth = strokeWidth

        // Apply padding to avoid clipping the stroke
        val extraPadding = (strokeWidth / 2).toInt()
        setPadding(paddingLeft + extraPadding, paddingTop + extraPadding, paddingRight + extraPadding, paddingBottom + extraPadding)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDraw(canvas: Canvas) {
        if (text.isNullOrEmpty()) return

        // Set up stroke and text paint properties
        strokePaint.textSize = textSize
        strokePaint.typeface = typeface

        val textPaint = paint.apply {
            style = Paint.Style.FILL
        }

        // Create or update StaticLayouts if needed
        if (strokeLayout == null || textLayout == null) {
            strokeLayout = createCenteredStaticLayout(strokePaint)
            textLayout = createCenteredStaticLayout(textPaint)
        }

        // Calculate offsets based on gravity
        val yOffset = calculateVerticalOffset()
        val xOffset = calculateHorizontalOffset()

        // Draw stroke text layout
        canvas.save()
        canvas.translate(xOffset, yOffset)
        strokeLayout?.draw(canvas)
        canvas.restore()

        // Draw main text layout
        canvas.save()
        canvas.translate(xOffset, yOffset)
        textLayout?.draw(canvas)
        canvas.restore()
    }

    // Helper method to create a StaticLayout with centered alignment
    @RequiresApi(Build.VERSION_CODES.M)
    private fun createCenteredStaticLayout(paint: TextPaint): StaticLayout {
        val width = width - paddingLeft - paddingRight
        return StaticLayout.Builder.obtain(text, 0, text.length, paint, width)
            .setAlignment(Layout.Alignment.ALIGN_CENTER) // Center align each line
            .setLineSpacing(0f, 1f)
            .setIncludePad(false)
            .build()
    }

    private fun calculateVerticalOffset(): Float {
        val totalTextHeight = textLayout?.height ?: 0
        val viewHeight = height - paddingTop - paddingBottom
        return when (gravity and Gravity.VERTICAL_GRAVITY_MASK) {
            Gravity.CENTER_VERTICAL -> (viewHeight - totalTextHeight) / 2f + paddingTop
            Gravity.TOP -> paddingTop.toFloat()
            Gravity.BOTTOM -> (viewHeight - totalTextHeight + paddingTop).toFloat()
            else -> paddingTop.toFloat()
        }
    }

    private fun calculateHorizontalOffset(): Float {
        return paddingLeft.toFloat() // Horizontal centering is handled within StaticLayout
    }

    fun setStrokeColor(color: Int) {
        strokeColor = color
        strokePaint.color = color
        strokeLayout = null // Recreate layout to apply new color
        invalidate()
    }

    fun setStrokeWidth(width: Float) {
        strokeWidth = width
        strokePaint.strokeWidth = width
        strokeLayout = null // Recreate layout to apply new stroke width
        invalidate()
    }
}
package com.fizzer.widget.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView

/**
 * @Author: Fizzer
 * @Email: fizzer503@sina.com
 * @Date: 2023/10/25
 * @Descriptor:
 */
class LetterIndexBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) :
    AppCompatTextView(context, attrs) {

    private var letters = arrayListOf(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
        "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    )

    /**
     * 文字画笔
     */
    private var mTextPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mCirclePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    @ColorInt
    var mTextColor = Color.parseColor("#000000")

    private var itemH = 0

    init {
        mTextPaint.color = mTextColor
        mTextPaint.textSize = textSize
        mTextPaint.textAlign = Paint.Align.CENTER
        mCirclePaint.color = Color.parseColor("#FF0000")
        mCirclePaint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawLetter(canvas)
    }


    private fun drawLetter(canvas: Canvas?) {
        val w = measuredWidth
        val h = measuredHeight
        itemH = h / letters.size
        val fontMetrics = mTextPaint.fontMetrics
        val posY = itemH / 2 + (fontMetrics.bottom - fontMetrics.top) / 2 - 10
        val radius = Math.min(w, itemH) / 2

        for ((index, letter) in letters.withIndex()) {

            canvas?.drawLine(
                0f,
                (itemH * index).toFloat(),
                w.toFloat(),
                (itemH * index).toFloat(),
                mTextPaint
            )
//            canvas?.drawCircle(
//                w / 2f,
//                (itemH/2 + itemH * index).toFloat(),
//                radius.toFloat(),
//                mCirclePaint
//            )
            canvas?.drawText(letter, w / 2f, posY + itemH * index, mTextPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }
}
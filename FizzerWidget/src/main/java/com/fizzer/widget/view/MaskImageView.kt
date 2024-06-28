package com.fizzer.widget.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap

/**
 * 仿刮刮乐开奖效果
 */
class MaskImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    private var mPaint: Paint = Paint().apply {
        isAntiAlias = true
        alpha = 0
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeWidth = 50f
        strokeCap = Paint.Cap.ROUND
    }

    private var mBgBitmap = drawable.toBitmap()
    private var mFgBitmap =
        Bitmap.createBitmap(mBgBitmap.width, mBgBitmap.height, Bitmap.Config.ARGB_8888)
    private var mPath = Path()
    private var mCanvas = Canvas(mFgBitmap).apply {
        drawColor(Color.GRAY)
    }

    //ImageView的原图
    val srcRect = Rect(0, 0, mBgBitmap.width, mBgBitmap.height)
    val outRect = Rect(0, 0, 0, 0)
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mPath.reset()
                mPath.moveTo(event.x, event.y)
            }

            MotionEvent.ACTION_MOVE -> {
                mPath.lineTo(event.x, event.y)
            }
        }
        mCanvas.drawPath(mPath, mPaint)
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawBitmap(
            mBgBitmap, srcRect,
            outRect.apply {
                right = this@MaskImageView.width
                bottom = this@MaskImageView.height
            }, null
        )
        canvas?.drawBitmap(mFgBitmap, 0f, 0f, null)
    }
}
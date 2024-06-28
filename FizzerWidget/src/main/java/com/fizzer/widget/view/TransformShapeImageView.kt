package com.fizzer.widget.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import com.fizzer.widget.R

/**
 * 可以实现异形变种的ImageView
 */
class TransformShapeImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    //绘制的整个视图View的矩阵大小
    private val mOutRect = Rect(0, 0, 0, 0)

    //绘制内容的矩阵大小，不包括边框，就是为了给边框留出一定的宽度
    private val mContentRect = Rect(0, 0, 0, 0)

    //ImageView需要加载的图片
    private val mDst = drawable.toBitmap()

    //imageView需要加载的图片矩阵
    private val mDstRect = Rect(0, 0, mDst.width, mDst.height)

    private val mPaint = Paint()


    //边框线的宽度
    private var strokeWidth = 2

    //边框线的颜色
    private var strokeColor = Color.parseColor("#FF0000")

    //maskView，用来显示ImageView的形状
    private var mMashView: Bitmap? = null

    //maskView的显示矩阵
    private val mMaskRect by lazy { Rect(0, 0, mMashView?.width ?: 0, mMashView?.height ?: 0) }

    init {
        initAttr(attrs)
    }

    /**
     * 初始化属性
     */
    private fun initAttr(attr: AttributeSet?) {

        attr?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.TransformShapeImageView)
            mMashView = ta.getDrawable(R.styleable.TransformShapeImageView_tsiv_mask)?.toBitmap()
            strokeWidth =
                ta.getDimension(R.styleable.TransformShapeImageView_tsiv_stroke_width, 2f).toInt()
            strokeColor =
                ta.getColor(R.styleable.TransformShapeImageView_tsiv_stroke_color, Color.WHITE)
            ta.recycle()
        }
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        val sc = canvas?.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null) ?: -1
        mOutRect.apply {
            right = this@TransformShapeImageView.width
            bottom = this@TransformShapeImageView.height
        }
        //指定绘制的大小
        mContentRect.apply {
            left += strokeWidth
            top += strokeWidth
            right = this@TransformShapeImageView.width - strokeWidth
            bottom = this@TransformShapeImageView.height - strokeWidth
        }
        mMashView?.let {
            canvas?.drawBitmap(it, mMaskRect, mContentRect, mPaint)
            mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        }
        canvas?.drawBitmap(mDst, mDstRect, mContentRect, mPaint)
        mMashView?.let {
            mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
            mPaint.colorFilter = PorterDuffColorFilter(strokeColor, PorterDuff.Mode.SRC_IN)
            canvas?.drawBitmap(it, mMaskRect, mOutRect, mPaint)
            mPaint.xfermode = null
            mPaint.colorFilter = null
        }

        canvas?.restoreToCount(sc)
    }
}
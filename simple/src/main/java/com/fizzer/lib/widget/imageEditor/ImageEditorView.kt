package com.fizzer.lib.widget.imageEditor

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.min

@SuppressLint("ViewConstructor")
class ImageEditorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val TAG = "ImageEditor"

    private var mClipRect: RectF = RectF()

    private var clipRectMargin = 40f

    private val mTransMatrix = Matrix()

    private var mBitmapWidth = 0
    private var mBitmapHeight = 0

    //记录上一次的点
    private val mLastPoint = PointF()


    private val mClipPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
        color = Color.WHITE
        strokeCap = Paint.Cap.SQUARE
    }

    private val mClipCornerPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = ImageEditConfig.CORNER_WIDTH
        color = Color.WHITE
        strokeCap = Paint.Cap.SQUARE
    }

    init {
        // 设置 scaleType 为 MATRIX，以便完全控制图像变换
        scaleType = ScaleType.MATRIX
        isFocusable = true
        isFocusableInTouchMode = true

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mClipRect.set(
            clipRectMargin,
            clipRectMargin,
            width - clipRectMargin,
            height - clipRectMargin
        )
        //确定控件的宽高后，根据当前控件的宽高进行图片的居中计算
        resetImageCenter()
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        bm?.let {
            mBitmapWidth = it.width
            mBitmapHeight = it.height
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawClipFrame(canvas)
    }

    var mTouchAnchor : PointF? = null
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mTouchAnchor = Anchor.instance.getAnchorByLocation(event.x, event.y)
                mLastPoint.set(event.x,event.y)
            }

            MotionEvent.ACTION_MOVE->{
                mTouchAnchor?.let {
                    val dx = event.x - mLastPoint.x
                    val dy = event.y - mLastPoint.y
                    mClipRect.set(mClipRect.left + dx,mClipRect.top + dy,mClipRect.right,mClipRect.bottom )
                    mLastPoint.set(event.x,event.y)
                    invalidate()
                }
            }
        }
        return true
    }
    /**
     * 设置图片居中
     */
    private fun resetImageCenter() {
        //按照裁剪框的比例，计算图片的缩放值，以将图片放进裁剪框中
        val scaleX = mClipRect.width() / mBitmapWidth
        val scaleY = mClipRect.height() / mBitmapHeight
        val resultScale = min(scaleX, scaleY)
        mTransMatrix.setScale(resultScale, resultScale)

        //计算bitmap的偏移值，进行居中处理
        val translateX = mClipRect.centerX() - mBitmapWidth * resultScale / 2
        val translateY = mClipRect.centerY() - mBitmapHeight * resultScale / 2
        mTransMatrix.postTranslate(translateX, translateY)
        imageMatrix = mTransMatrix
    }


    //绘制裁剪框
    private fun drawClipFrame(canvas: Canvas?) {
        canvas?.drawRect(mClipRect, mClipPaint)
        //绘制的时候，重置锚点位置
        Anchor.instance.resetAnchor(mClipRect)

        val topLeftAnchor = Anchor.instance.getLeftTopAnchor()
        //左上角
        canvas?.drawLine(
            topLeftAnchor.x, topLeftAnchor.y,
            topLeftAnchor.x + ImageEditConfig.CORNER_LENGTH, topLeftAnchor.y,
            mClipCornerPaint
        )
        canvas?.drawLine(
            topLeftAnchor.x, topLeftAnchor.y,
            topLeftAnchor.x, topLeftAnchor.y + ImageEditConfig.CORNER_LENGTH,
            mClipCornerPaint
        )

        //右上角
        val topRightAnchor = Anchor.instance.getRightTopAnchor()
        canvas?.drawLine(
            topRightAnchor.x, topRightAnchor.y,
            topRightAnchor.x - ImageEditConfig.CORNER_LENGTH, topRightAnchor.y,
            mClipCornerPaint
        )
        canvas?.drawLine(
            topRightAnchor.x, topRightAnchor.y,
            topRightAnchor.x, topRightAnchor.y + ImageEditConfig.CORNER_LENGTH,
            mClipCornerPaint
        )

        //左下角
        val bottomLeftAnchor = Anchor.instance.getLeftBottomAnchor()
        canvas?.drawLine(
            bottomLeftAnchor.x, bottomLeftAnchor.y,
            bottomLeftAnchor.x + ImageEditConfig.CORNER_LENGTH, bottomLeftAnchor.y,
            mClipCornerPaint
        )
        canvas?.drawLine(
            bottomLeftAnchor.x, bottomLeftAnchor.y,
            bottomLeftAnchor.x, bottomLeftAnchor.y - ImageEditConfig.CORNER_LENGTH,
            mClipCornerPaint
        )

        //右下角
        val bottomRightAnchor = Anchor.instance.getRightBottomAnchor()
        canvas?.drawLine(
            bottomRightAnchor.x, bottomRightAnchor.y,
            bottomRightAnchor.x - ImageEditConfig.CORNER_LENGTH, bottomRightAnchor.y,
            mClipCornerPaint
        )

        canvas?.drawLine(
            bottomRightAnchor.x, bottomRightAnchor.y,
            bottomRightAnchor.x, bottomRightAnchor.y - ImageEditConfig.CORNER_LENGTH,
            mClipCornerPaint
        )
    }
}
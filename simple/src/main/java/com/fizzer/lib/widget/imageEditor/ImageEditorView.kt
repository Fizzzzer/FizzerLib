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
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.min

@SuppressLint("ViewConstructor")
class ImageEditorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val TAG = "ImageEditor"


    private val mTransMatrix = Matrix()

    private var mBitmapWidth = 0
    private var mBitmapHeight = 0

    //记录上一次的点
    private val mLastPoint = PointF()

    init {
        // 设置 scaleType 为 MATRIX，以便完全控制图像变换
        scaleType = ScaleType.MATRIX
        isFocusable = true
        isFocusableInTouchMode = true

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        ClipFrameView.instance.initClipFrameSize(width = width.toFloat(), height = height.toFloat())
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
        ClipFrameView.instance.onDraw(canvas)
    }

    var mTouchAnchor: Anchor? = null
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mTouchAnchor = ClipFrameView.instance.getAnchorByLocation(event.x,event.y)
                Log.e(TAG,"按下 = ${mTouchAnchor?.mType}")
                mLastPoint.set(event.x, event.y)
            }

            MotionEvent.ACTION_MOVE -> {
                mTouchAnchor?.let {
                    val dx = event.x - mLastPoint.x
                    val dy = event.y - mLastPoint.y
                    //移动锚点
                    ClipFrameView.instance.anchorMove(it,dx,dy)
                    mLastPoint.set(event.x, event.y)
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
        val scaleX = ClipFrameView.instance.getClipRect().width() / mBitmapWidth
        val scaleY = ClipFrameView.instance.getClipRect().height() / mBitmapHeight
        val resultScale = min(scaleX, scaleY)
        mTransMatrix.setScale(resultScale, resultScale)

        //计算bitmap的偏移值，进行居中处理
        val translateX =
            ClipFrameView.instance.getClipRect().centerX() - mBitmapWidth * resultScale / 2
        val translateY =
            ClipFrameView.instance.getClipRect().centerY() - mBitmapHeight * resultScale / 2
        mTransMatrix.postTranslate(translateX, translateY)
        imageMatrix = mTransMatrix
    }
}
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
import androidx.core.graphics.drawable.toBitmap
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

@SuppressLint("ViewConstructor")
class ImageEditorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val TAG = "ImageEditor"


    private val mTransMatrix = Matrix()

    private val mSaveMatrix = Matrix()

    private var mBitmapWidth = 0
    private var mBitmapHeight = 0

    //当前的缩放
    private var currentScale = 1f

    //当前的旋转
    private var currentRotation = 0f

    //两点的终点，缩放时 记录两点的中点
    private var mMidPoint: PointF = PointF()
    private var oldDist = 1f
    private var oldRotation = 0f

    private val MODE_CLIP = "CLIP"
    private val MODE_ZOOM = "ZOOM"
    private val MODE_DRAG = "DRAG"


    private var mMode = MODE_CLIP

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
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                mTouchAnchor = ClipFrameView.instance.getAnchorByLocation(event.x, event.y)
                Log.e(TAG, "按下 = ${mTouchAnchor?.mType}")
                if (mTouchAnchor != null) {
                    mMode = MODE_CLIP
                } else {
                    mMode = MODE_DRAG
                    mSaveMatrix.set(mTransMatrix)
                }
                mLastPoint.set(event.x, event.y)
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount == 2) {
                    oldDist = spacing(event)
                    if (oldDist > 10f) {
                        mSaveMatrix.set(mTransMatrix)
                        midPoint(mMidPoint, event)
                        oldRotation = rotation(event)
                        mMode = MODE_ZOOM
                    }
                }
            }

            MotionEvent.ACTION_MOVE -> {
                when (mMode) {

                    MODE_DRAG -> {
                        mTransMatrix.set(mSaveMatrix)
                        val dx = event.x - mLastPoint.x
                        val dy = event.y - mLastPoint.y
                        mTransMatrix.postTranslate(dx, dy)
                        imageMatrix = mTransMatrix
                    }

                    MODE_ZOOM -> {
                        if (event.pointerCount == 2) {
                            val newDist = spacing(event)
                            val newRotate = rotation(event)
                            mTransMatrix.set(mSaveMatrix)

                            //缩放
                            val scale = newDist / oldDist
                            currentScale *= scale
                            mTransMatrix.postScale(scale, scale, mMidPoint.x, mMidPoint.y)

                            //旋转
                            val rotate = newRotate - oldRotation
                            currentRotation += rotate
                            mTransMatrix.postRotate(rotate, mMidPoint.x, mMidPoint.y)
                            imageMatrix = mTransMatrix
                        }
                    }

                    MODE_CLIP -> {
                        mTouchAnchor?.let {
                            val dx = event.x - mLastPoint.x
                            val dy = event.y - mLastPoint.y
                            //移动锚点
                            ClipFrameView.instance.anchorMove(it, dx, dy)
                            mLastPoint.set(event.x, event.y)
                        }
                    }
                }
            }
        }

        invalidate()
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


    // 计算两点间距离
    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt(x * x + y * y)
    }

    // 计算两点间角度
    private fun rotation(event: MotionEvent): Float {
        val deltaX = (event.getX(0) - event.getX(1))
        val deltaY = (event.getY(0) - event.getY(1))
        return Math.toDegrees(Math.atan2(deltaY.toDouble(), deltaX.toDouble())).toFloat()
    }

    // 计算两点的中点
    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point.x = x / 2f
        point.y = y / 2f
    }

    fun rotateImage() {
        mSaveMatrix.set(mTransMatrix)
        currentRotation += 90f
        mTransMatrix.postRotate(90f, width / 2f, height / 2f)
        imageMatrix = mTransMatrix
        invalidate()
    }

    fun getClipBitmap(): Bitmap? {

//        val mDrawable = drawable ?: return null
//        // 获取当前图片的矩阵值
//        val values = FloatArray(9)
//        imageMatrix.getValues(values)
//        // 计算逆矩阵，用于将裁剪框坐标转换到图片原始坐标
//        val inverseMatrix = Matrix()
//        imageMatrix.invert(inverseMatrix)
//        // 将裁剪框坐标转换到图片原始坐标
//        val cropPoints = floatArrayOf(
//            ClipFrameView.instance.getClipRect().left, ClipFrameView.instance.getClipRect().top,
//            ClipFrameView.instance.getClipRect().right, ClipFrameView.instance.getClipRect().bottom
//        )
//
//        inverseMatrix.mapPoints(cropPoints)
//        // 计算裁剪区域在原图中的坐标
//        val srcLeft = cropPoints[0].toInt().coerceIn(0, drawable.intrinsicWidth)
//        val srcTop = cropPoints[1].toInt().coerceIn(0, drawable.intrinsicHeight)
//        val srcRight = cropPoints[2].toInt().coerceIn(srcLeft, drawable.intrinsicWidth)
//        val srcBottom = cropPoints[3].toInt().coerceIn(srcTop, drawable.intrinsicHeight)
//
//        // 确保裁剪区域有效
//        val width = srcRight - srcLeft
//        val height = srcBottom - srcTop
//        if (width <= 0 || height <= 0) return null
//
//        // 创建并返回裁剪后的 Bitmap
//        return Bitmap.createBitmap(
//            drawable.toBitmap(),
//            srcLeft,
//            srcTop,
//            width,
//            height
//        )

        val mDrawable = drawable ?: return null

        // 创建临时 Bitmap，尺寸为原图大小
        val tempBitmap = Bitmap.createBitmap(
            mDrawable.intrinsicWidth,
            mDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val tempCanvas = Canvas(tempBitmap)

        // 应用当前矩阵到临时 Canvas
        tempCanvas.concat(mTransMatrix)

        // 绘制原图到临时 Bitmap（自动应用旋转）
        mDrawable.setBounds(0, 0, mDrawable.intrinsicWidth, mDrawable.intrinsicHeight)
        mDrawable.draw(tempCanvas)

        // 计算裁剪框在临时 Bitmap 中的相对位置（考虑初始缩放和居中）
        val scale = Matrix()
        scale.setScale(currentScale,currentScale)
        mTransMatrix.invert(scale)
//        val cropPoints = floatArrayOf(
//            cropRect.left, cropRect.top,
//            cropRect.right, cropRect.bottom
//        )
//        scale.mapPoints(cropPoints)
        val tmpClipRect = RectF()
        tmpClipRect.set(ClipFrameView.instance.getClipRect())
        scale.mapRect(tmpClipRect)

        val left = tmpClipRect.left.toInt().coerceIn(0, tempBitmap.width)
        val top = tmpClipRect.top.toInt().coerceIn(0, tempBitmap.height)
        val right = tmpClipRect.right.toInt().coerceIn(left, tempBitmap.width)
        val bottom = tmpClipRect.bottom.toInt().coerceIn(top, tempBitmap.height)

        val width = right - left
        val height = bottom - top
        if (width <= 0 || height <= 0) {
            tempBitmap.recycle()
            return null
        }

        // 从临时 Bitmap 中截取裁剪区域
        val croppedBitmap = Bitmap.createBitmap(tempBitmap, left, top, width, height)
        tempBitmap.recycle()  // 释放临时资源

        return croppedBitmap

    }
}
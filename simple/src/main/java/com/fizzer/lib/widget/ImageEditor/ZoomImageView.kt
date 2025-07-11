package com.fizzer.lib.widget.ImageEditor

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import kotlin.math.max
import kotlin.math.min

class ZoomImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 图片相关属性
    private var bitmap: Bitmap? = null
    private val imageMatrix = Matrix()
    private val initialMatrix = Matrix()
    private val inverseMatrix = Matrix()
    private val imageRect = RectF()
    private val drawRect = RectF()

    // 手势检测
    private val scaleDetector: ScaleGestureDetector
    private val gestureDetector: GestureDetector
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var isScaling = false

    // 最小/最大缩放比例
    private var minScale = 1.0f
    private var maxScale = 8.0f
    private var currentScale = 1.0f

    // 接口
    var onImageChangedListener: (() -> Unit)? = null

    init {
        scaleDetector = ScaleGestureDetector(context, ScaleListener())
        gestureDetector = GestureDetector(context, GestureListener())
    }

    fun setImageBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
        resetView()
        invalidate()
    }

    fun getImageMatrix() = Matrix(imageMatrix)

    fun resetView() {
        bitmap?.let { bmp ->
            val viewWidth = width.toFloat()
            val viewHeight = height.toFloat()

            // 计算初始缩放比例
            minScale = min(viewWidth / bmp.width, viewHeight / bmp.height)
            currentScale = minScale

            // 重置变换矩阵
            imageMatrix.reset()
            imageMatrix.postScale(minScale, minScale)
            imageMatrix.postTranslate(
                (viewWidth - bmp.width * minScale) / 2,
                (viewHeight - bmp.height * minScale) / 2
            )

            initialMatrix.set(imageMatrix)
            notifyImageChanged()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        resetView()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        bitmap?.let { bmp ->
            canvas.save()
            canvas.concat(imageMatrix)
            canvas.drawBitmap(bmp, 0f, 0f, null)
            canvas.restore()

            // 更新绘制区域
            imageRect.set(0f, 0f, bmp.width.toFloat(), bmp.height.toFloat())
            imageMatrix.mapRect(drawRect, imageRect)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)

        val x = event.x
        val y = event.y

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchX = x
                lastTouchY = y
            }

            MotionEvent.ACTION_MOVE -> {
                if (!scaleDetector.isInProgress) {
                    val dx = x - lastTouchX
                    val dy = y - lastTouchY
                    moveImage(dx, dy)
                }
                lastTouchX = x
                lastTouchY = y
            }
        }

        return true
    }

    private fun moveImage(dx: Float, dy: Float) {
        // 检查边界，防止图片移出视图
        val values = FloatArray(9)
        imageMatrix.getValues(values)
        val currentX = values[Matrix.MTRANS_X]
        val currentY = values[Matrix.MTRANS_Y]

        var newDx = dx
        var newDy = dy

        if (currentX + dx > 0) newDx = -currentX
        if (currentX + dx < width - drawRect.width()) newDx = width - drawRect.width() - currentX
        if (currentY + dy > 0) newDy = -currentY
        if (currentY + dy < height - drawRect.height()) newDy = height - drawRect.height() - currentY

        imageMatrix.postTranslate(newDx, newDy)
        invalidate()
        notifyImageChanged()
    }

    fun zoomToRect(targetRect: RectF) {
        val currentRect = RectF()
        imageMatrix.mapRect(currentRect, imageRect)

        // 计算缩放比例
        val scaleX = width / targetRect.width()
        val scaleY = height / targetRect.height()
        val scale = min(scaleX, scaleY) * 0.95f

        // 计算目标位置
        val targetCenterX = targetRect.centerX()
        val targetCenterY = targetRect.centerY()

        // 应用缩放和平移
        imageMatrix.postScale(scale, scale, targetCenterX, targetCenterY)

        // 调整位置确保在视图内
        val values = FloatArray(9)
        imageMatrix.getValues(values)
        val currentX = values[Matrix.MTRANS_X]
        val currentY = values[Matrix.MTRANS_Y]
        val scaledWidth = drawRect.width() * scale
        val scaledHeight = drawRect.height() * scale

        var offsetX = 0f
        var offsetY = 0f

        if (currentX > 0) offsetX = -currentX
        else if (currentX + scaledWidth < width) offsetX = width - (currentX + scaledWidth)

        if (currentY > 0) offsetY = -currentY
        else if (currentY + scaledHeight < height) offsetY = height - (currentY + scaledHeight)

        if (offsetX != 0f || offsetY != 0f) {
            imageMatrix.postTranslate(offsetX, offsetY)
        }

        currentScale = scale
        invalidate()
        notifyImageChanged()
    }

    private fun notifyImageChanged() {
        onImageChangedListener?.invoke()
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scaleFactor = detector.scaleFactor
            val newScale = currentScale * scaleFactor

            if (newScale < minScale || newScale > maxScale) {
                return true
            }

            val focusX = detector.focusX
            val focusY = detector.focusY

            imageMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY)
            currentScale = newScale
            invalidate()
            notifyImageChanged()
            return true
        }
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            resetView()
            invalidate()
            return true
        }
    }

    fun getImageDrawRect() = RectF(drawRect)
}
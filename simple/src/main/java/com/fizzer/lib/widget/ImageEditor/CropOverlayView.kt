package com.fizzer.lib.widget.ImageEditor

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class CropOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 裁剪框属性
    private val cropRect = RectF()
    private var initialCropRect = RectF()
    private var cropRatio = 1f // 默认1:1比例
    private var isResizingCrop = false
    private var resizeEdge = 0
    private val handleRadius = 30f
    private val minCropSize = 150f

    // 图片信息
    private var imageRect = RectF()
    private var imageMatrix = Matrix()

    // 动画
    private var zoomAnimator: ValueAnimator? = null

    // 画笔
    private val cropPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 3f
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
    }

    private val overlayPaint = Paint().apply {
        color = Color.argb(150, 0, 0, 0)
        style = Paint.Style.FILL
    }

    private val handlePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val handleStrokePaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

    // 预设比例
    enum class CropRatio(val ratio: Float, val display: String) {
        RATIO_FREE(0f, "自由"),
        RATIO_1_1(1f, "1:1"),
        RATIO_4_3(4f/3f, "4:3"),
        RATIO_3_2(3f/2f, "3:2"),
        RATIO_16_9(16f/9f, "16:9")
    }

    // 回调接口
    var onCropRectChanged: ((RectF) -> Unit)? = null
    var onCropRectFinalized: ((RectF) -> Unit)? = null

    fun setCropRatio(ratio: Float) {
        cropRatio = ratio
        adjustCropRectToRatio()
        invalidate()
    }

    fun setImageInfo(rect: RectF, matrix: Matrix) {
        imageRect = rect
        imageMatrix = matrix
        if (cropRect.isEmpty) {
            initCropRect()
        }
    }

    private fun initCropRect() {
        if (imageRect.width() <= 0 || imageRect.height() <= 0) return

        val centerX = imageRect.centerX()
        val centerY = imageRect.centerY()
        val cropSize = min(imageRect.width(), imageRect.height()) * 0.6f

        cropRect.set(
            centerX - cropSize / 2,
            centerY - cropSize / 2,
            centerX + cropSize / 2,
            centerY + cropSize / 2
        )

        adjustCropRectToRatio()
        initialCropRect.set(cropRect)
    }

    private fun adjustCropRectToRatio() {
        if (cropRatio <= 0) return

        val centerX = cropRect.centerX()
        val centerY = cropRect.centerY()
        val width = cropRect.width()
        val height = cropRect.height()
        val currentRatio = width / height

        if (currentRatio > cropRatio) {
            // 调整高度
            val newHeight = width / cropRatio
            cropRect.top = centerY - newHeight / 2
            cropRect.bottom = centerY + newHeight / 2
        } else {
            // 调整宽度
            val newWidth = height * cropRatio
            cropRect.left = centerX - newWidth / 2
            cropRect.right = centerX + newWidth / 2
        }

        constrainCropRect()
        invalidate()
    }

    private fun constrainCropRect() {
        cropRect.left = max(imageRect.left, min(cropRect.left, imageRect.right - minCropSize))
        cropRect.top = max(imageRect.top, min(cropRect.top, imageRect.bottom - minCropSize))
        cropRect.right = min(imageRect.right, max(cropRect.right, imageRect.left + minCropSize))
        cropRect.bottom = min(imageRect.bottom, max(cropRect.bottom, imageRect.top + minCropSize))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制半透明覆盖层（裁剪框外部）
        canvas.drawRect(0f, 0f, width.toFloat(), cropRect.top, overlayPaint)
        canvas.drawRect(0f, cropRect.bottom, width.toFloat(), height.toFloat(), overlayPaint)
        canvas.drawRect(0f, cropRect.top, cropRect.left, cropRect.bottom, overlayPaint)
        canvas.drawRect(cropRect.right, cropRect.top, width.toFloat(), cropRect.bottom, overlayPaint)

        // 绘制裁剪框
        canvas.drawRect(cropRect, cropPaint)

        // 绘制调整手柄
        if (cropRatio <= 0) {
            drawHandle(canvas, cropRect.left, cropRect.top) // 左上
            drawHandle(canvas, cropRect.right, cropRect.top) // 右上
            drawHandle(canvas, cropRect.left, cropRect.bottom) // 左下
            drawHandle(canvas, cropRect.right, cropRect.bottom) // 右下
        }
    }

    private fun drawHandle(canvas: Canvas, x: Float, y: Float) {
        canvas.drawCircle(x, y, handleRadius, handlePaint)
        canvas.drawCircle(x, y, handleRadius, handleStrokePaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // 检查是否点在调整手柄上
                if (cropRatio <= 0) {
                    resizeEdge = getTouchedHandle(x, y)
                    if (resizeEdge != 0) {
                        isResizingCrop = true
                        return true
                    }
                }

                // 检查是否点在裁剪框内
                if (cropRect.contains(x, y)) {
                    return true
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (isResizingCrop) {
                    resizeCropRect(x, y)
                    onCropRectChanged?.invoke(cropRect)
                    invalidate()
                    return true
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (isResizingCrop) {
                    isResizingCrop = false
                    resizeEdge = 0
                    autoZoomToFit()
                }
            }
        }

        return super.onTouchEvent(event)
    }

    private fun getTouchedHandle(x: Float, y: Float): Int {
        val touchTolerance = handleRadius * 1.5f

        // 左上角
        if (abs(x - cropRect.left) < touchTolerance && abs(y - cropRect.top) < touchTolerance) {
            return 1
        }
        // 右上角
        if (abs(x - cropRect.right) < touchTolerance && abs(y - cropRect.top) < touchTolerance) {
            return 2
        }
        // 左下角
        if (abs(x - cropRect.left) < touchTolerance && abs(y - cropRect.bottom) < touchTolerance) {
            return 3
        }
        // 右下角
        if (abs(x - cropRect.right) < touchTolerance && abs(y - cropRect.bottom) < touchTolerance) {
            return 4
        }

        return 0
    }

    private fun resizeCropRect(x: Float, y: Float) {
        when (resizeEdge) {
            1 -> { // 左上角
                cropRect.left = x
                cropRect.top = y
            }
            2 -> { // 右上角
                cropRect.right = x
                cropRect.top = y
            }
            3 -> { // 左下角
                cropRect.left = x
                cropRect.bottom = y
            }
            4 -> { // 右下角
                cropRect.right = x
                cropRect.bottom = y
            }
        }

        // 确保裁剪框有效
        if (cropRect.width() < minCropSize) {
            when (resizeEdge) {
                1, 3 -> cropRect.left = cropRect.right - minCropSize
                2, 4 -> cropRect.right = cropRect.left + minCropSize
            }
        }

        if (cropRect.height() < minCropSize) {
            when (resizeEdge) {
                1, 2 -> cropRect.top = cropRect.bottom - minCropSize
                3, 4 -> cropRect.bottom = cropRect.top + minCropSize
            }
        }

        // 确保裁剪框在图片范围内
        constrainCropRect()

        // 如果有固定比例，保持比例
        if (cropRatio > 0) {
            adjustCropRectToRatio()
        }
    }

    private fun abs(value: Float) = if (value < 0) -value else value

    fun getCropRect() = RectF(cropRect)

    private fun autoZoomToFit() {
        // 保存当前裁剪框位置
        val currentCropRect = RectF(cropRect)

        // 计算目标裁剪框大小（屏幕宽度的90%）
        val targetWidth = width * 0.9f
        val targetHeight = if (cropRatio > 0) {
            targetWidth / cropRatio
        } else {
            currentCropRect.height() * (targetWidth / currentCropRect.width())
        }

        // 计算目标位置（居中）
        val targetLeft = (width - targetWidth) / 2
        val targetTop = (height - targetHeight) / 2
        val targetRect = RectF(targetLeft, targetTop, targetLeft + targetWidth, targetTop + targetHeight)

        // 启动动画
        zoomAnimator?.cancel()
        zoomAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 300
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation ->
                val progress = animation.animatedValue as Float

                // 插值计算当前裁剪框位置
                cropRect.left = currentCropRect.left + (targetRect.left - currentCropRect.left) * progress
                cropRect.top = currentCropRect.top + (targetRect.top - currentCropRect.top) * progress
                cropRect.right = currentCropRect.right + (targetRect.right - currentCropRect.right) * progress
                cropRect.bottom = currentCropRect.bottom + (targetRect.bottom - currentCropRect.bottom) * progress

                invalidate()
                onCropRectChanged?.invoke(cropRect)

                // 动画结束时通知外部
                if (progress == 1f) {
                    onCropRectFinalized?.invoke(cropRect)
                }
            }
            start()
        }
    }
}
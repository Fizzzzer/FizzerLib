package com.fizzer.lib.widget.RecyclerView

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import kotlin.math.min
import kotlin.math.sqrt

class CropImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    // 常量定义
    private val SNAP_RANGE = 20f  // 吸附范围
    private val MIN_CROP_SIZE = 100f  // 最小裁剪尺寸

    // 变换矩阵
    private val imageMatrix = Matrix()
    private val savedMatrix = Matrix()

    // 状态常量
    private val NONE = 0
    private val DRAG = 1
    private val ZOOM = 2
    private val ROTATE = 3
    private val RESIZE_CROP = 4

    // 当前状态
    private var mode = NONE

    // 触摸点坐标
    private val start = FloatArray(2)
    private val mid = FloatArray(2)
    private var oldDist = 1f
    private var oldRotation = 0f

    // 图片属性
    private var imageWidth = 0f
    private var imageHeight = 0f
    private var currentScale = 1f
    private var currentRotation = 0f

    // 裁剪框属性
    private var cropRect = RectF()
    private var initialCropRect = RectF()
    private var activeResizeEdge = -1  // 0-3 分别表示左、上、右、下边界

    // 边缘检测矩形
    private val leftEdge = RectF()
    private val topEdge = RectF()
    private val rightEdge = RectF()
    private val bottomEdge = RectF()

    // 画笔
    private val cropRectPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 2f
        isAntiAlias = true
    }

    private val cropGridPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 1f
        pathEffect = DashPathEffect(floatArrayOf(5f, 5f), 0f)
    }

    private val cornerPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 5f
        isAntiAlias = true
    }

    init {
        // 设置 scaleType 为 MATRIX，以便完全控制图像变换
        scaleType = ScaleType.MATRIX
        isFocusable = true
        isFocusableInTouchMode = true
    }

    // 设置图片
    override fun setImageBitmap(bitmap: Bitmap) {
        super.setImageBitmap(bitmap)
//        // 初始化图片属性
        imageWidth = bitmap.width.toFloat()
        imageHeight = bitmap.height.toFloat()
        // 初始化变换矩阵
        resetMatrix()
        // 初始化裁剪框
        initCropRect()
        // 重绘
        invalidate()
    }

    // 重置矩阵到初始状态
    private fun resetMatrix() {
        imageMatrix.reset()
        // 计算初始缩放比例，使图片适应视图
        val scaleX = width.toFloat() / imageWidth
        val scaleY = height.toFloat() / imageHeight
        val initScale = min(scaleX, scaleY) * 0.9f  // 留一点边距

        currentScale = initScale
        currentRotation = 0f

        // 缩放并居中
        imageMatrix.setScale(initScale, initScale)
        val dx = (width - imageWidth * initScale) / 2
        val dy = (height - imageHeight * initScale) / 2
        imageMatrix.postTranslate(dx, dy)

        setImageMatrix(imageMatrix)
    }

    // 初始化裁剪框（默认为视图中心的 80% 大小）
    private fun initCropRect() {
        val cropWidth = width * 0.8f
        val cropHeight = height * 0.8f
        val left = (width - cropWidth) / 2
        val top = (height - cropHeight) / 2

        cropRect.set(left, top, left + cropWidth, top + cropHeight)
        initialCropRect.set(cropRect)
    }

    // 处理触摸事件
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                // 检查是否触摸裁剪框边缘
                activeResizeEdge = getResizeEdge(event.x, event.y)
                if (activeResizeEdge != -1) {
                    mode = RESIZE_CROP
                    start[0] = event.x
                    start[1] = event.y
                } else {
                    // 触摸图片，开始拖动
                    savedMatrix.set(imageMatrix)
                    start[0] = event.x
                    start[1] = event.y
                    mode = DRAG
                }
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                // 多点触控，开始缩放或旋转
                if (event.pointerCount == 2) {
                    oldDist = spacing(event)
                    if (oldDist > 10f) {
                        savedMatrix.set(imageMatrix)
                        midPoint(mid, event)
                        oldRotation = rotation(event)
                        mode = ZOOM
                    }
                }
            }

            MotionEvent.ACTION_MOVE -> {
                when (mode) {
                    DRAG -> {
                        // 拖动图片
                        imageMatrix.set(savedMatrix)
                        val dx = event.x - start[0]
                        val dy = event.y - start[1]
                        imageMatrix.postTranslate(dx, dy)
                        setImageMatrix(imageMatrix)
                    }

                    ZOOM -> {
                        // 缩放或旋转图片
                        if (event.pointerCount == 2) {
                            val newDist = spacing(event)
                            val newRotation = rotation(event)

                            imageMatrix.set(savedMatrix)

                            // 缩放
                            val scale = newDist / oldDist
                            currentScale *= scale
                            imageMatrix.postScale(scale, scale, mid[0], mid[1])

                            // 旋转
                            val rotate = newRotation - oldRotation
                            currentRotation += rotate
                            imageMatrix.postRotate(rotate, mid[0], mid[1])

                            setImageMatrix(imageMatrix)
                        }
                    }

                    RESIZE_CROP -> {
                        // 调整裁剪框大小
                        val dx = event.x - start[0]
                        val dy = event.y - start[1]

                        when (activeResizeEdge) {
                            0 -> updateCropRect(cropRect.left + dx, cropRect.top, cropRect.right, cropRect.bottom)
                            1 -> updateCropRect(cropRect.left, cropRect.top + dy, cropRect.right, cropRect.bottom)
                            2 -> updateCropRect(cropRect.left, cropRect.top, cropRect.right + dx, cropRect.bottom)
                            3 -> updateCropRect(cropRect.left, cropRect.top, cropRect.right, cropRect.bottom + dy)
                        }

                        start[0] = event.x
                        start[1] = event.y
                    }
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                mode = NONE
            }
        }

        invalidate()
        return true
    }

    // 更新裁剪框
    private fun updateCropRect(left: Float, top: Float, right: Float, bottom: Float) {
        // 确保裁剪框不小于最小尺寸
        if (right - left < MIN_CROP_SIZE) return
        if (bottom - top < MIN_CROP_SIZE) return

        // 确保裁剪框在视图范围内
        val newLeft = left.coerceIn(0f, width - MIN_CROP_SIZE)
        val newTop = top.coerceIn(0f, height - MIN_CROP_SIZE)
        val newRight = right.coerceIn(newLeft + MIN_CROP_SIZE, width.toFloat())
        val newBottom = bottom.coerceIn(newTop + MIN_CROP_SIZE, height.toFloat())

        cropRect.set(newLeft, newTop, newRight, newBottom)
    }

    // 检查触摸点是否在裁剪框边缘
    private fun getResizeEdge(x: Float, y: Float): Int {
        // 更新边缘检测矩形
        val edgeSize = 20f  // 边缘检测宽度

        leftEdge.set(cropRect.left - edgeSize, cropRect.top, cropRect.left + edgeSize, cropRect.bottom)
        topEdge.set(cropRect.left, cropRect.top - edgeSize, cropRect.right, cropRect.top + edgeSize)
        rightEdge.set(cropRect.right - edgeSize, cropRect.top, cropRect.right + edgeSize, cropRect.bottom)
        bottomEdge.set(cropRect.left, cropRect.bottom - edgeSize, cropRect.right, cropRect.bottom + edgeSize)

        // 检查边缘
        if (leftEdge.contains(x, y)) return 0
        if (topEdge.contains(x, y)) return 1
        if (rightEdge.contains(x, y)) return 2
        if (bottomEdge.contains(x, y)) return 3

        return -1
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
    private fun midPoint(point: FloatArray, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point[0] = x / 2f
        point[1] = y / 2f
    }

    // 绘制裁剪框和辅助线
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制裁剪框
        canvas.drawRect(cropRect, cropRectPaint)

        // 绘制九宫格辅助线
        val thirdWidth = cropRect.width() / 3
        val thirdHeight = cropRect.height() / 3

        // 垂直线
        canvas.drawLine(
            cropRect.left + thirdWidth,
            cropRect.top,
            cropRect.left + thirdWidth,
            cropRect.bottom,
            cropGridPaint
        )
        canvas.drawLine(
            cropRect.left + 2 * thirdWidth,
            cropRect.top,
            cropRect.left + 2 * thirdWidth,
            cropRect.bottom,
            cropGridPaint
        )

        // 水平线
        canvas.drawLine(
            cropRect.left,
            cropRect.top + thirdHeight,
            cropRect.right,
            cropRect.top + thirdHeight,
            cropGridPaint
        )
        canvas.drawLine(
            cropRect.left,
            cropRect.top + 2 * thirdHeight,
            cropRect.right,
            cropRect.top + 2 * thirdHeight,
            cropGridPaint
        )

        // 绘制四个角
        val cornerLength = 20f

        // 左上角
        canvas.drawLine(cropRect.left, cropRect.top, cropRect.left + cornerLength, cropRect.top, cornerPaint)
        canvas.drawLine(cropRect.left, cropRect.top, cropRect.left, cropRect.top + cornerLength, cornerPaint)

        // 右上角
        canvas.drawLine(cropRect.right, cropRect.top, cropRect.right - cornerLength, cropRect.top, cornerPaint)
        canvas.drawLine(cropRect.right, cropRect.top, cropRect.right, cropRect.top + cornerLength, cornerPaint)

        // 左下角
        canvas.drawLine(cropRect.left, cropRect.bottom, cropRect.left + cornerLength, cropRect.bottom, cornerPaint)
        canvas.drawLine(cropRect.left, cropRect.bottom, cropRect.left, cropRect.bottom - cornerLength, cornerPaint)

        // 右下角
        canvas.drawLine(cropRect.right, cropRect.bottom, cropRect.right - cornerLength, cropRect.bottom, cornerPaint)
        canvas.drawLine(cropRect.right, cropRect.bottom, cropRect.right, cropRect.bottom - cornerLength, cornerPaint)
    }

    // 执行裁剪，返回裁剪后的 Bitmap
    fun getCroppedBitmap(): Bitmap? {
        val drawable = drawable ?: return null

        // 获取当前图片的矩阵值
        val values = FloatArray(9)
        imageMatrix.getValues(values)

        // 计算逆矩阵，用于将裁剪框坐标转换到图片原始坐标
        val inverseMatrix = Matrix()
        imageMatrix.invert(inverseMatrix)

        // 将裁剪框坐标转换到图片原始坐标
        val cropPoints = floatArrayOf(
            cropRect.left, cropRect.top,
            cropRect.right, cropRect.bottom
        )
        inverseMatrix.mapPoints(cropPoints)

        // 计算裁剪区域在原图中的坐标
        val srcLeft = cropPoints[0].toInt().coerceIn(0, drawable.intrinsicWidth)
        val srcTop = cropPoints[1].toInt().coerceIn(0, drawable.intrinsicHeight)
        val srcRight = cropPoints[2].toInt().coerceIn(srcLeft, drawable.intrinsicWidth)
        val srcBottom = cropPoints[3].toInt().coerceIn(srcTop, drawable.intrinsicHeight)

        // 确保裁剪区域有效
        val width = srcRight - srcLeft
        val height = srcBottom - srcTop
        if (width <= 0 || height <= 0) return null

        // 创建并返回裁剪后的 Bitmap
        return Bitmap.createBitmap(
            drawable.toBitmap(),
            srcLeft,
            srcTop,
            width,
            height
        )
    }

    // 旋转图片 90 度
    fun rotateImage() {
        savedMatrix.set(imageMatrix)
        currentRotation += 90f
        imageMatrix.postRotate(90f, width / 2f, height / 2f)
        setImageMatrix(imageMatrix)
        invalidate()
    }

    // 重置裁剪框和图片到初始状态
    fun reset() {
        resetMatrix()
        cropRect.set(initialCropRect)
        invalidate()
    }
}
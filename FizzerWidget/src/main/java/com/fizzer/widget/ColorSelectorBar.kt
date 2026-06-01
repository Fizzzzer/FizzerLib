package com.fizzer.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class ColorSelectorBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mPointerX = 0f
    private var mPointerY = 0f
    private val HORIZONTAL = 1
    private val VERTICAL = 2

    private var mOrientation = VERTICAL

    private val mDefaultViewWidth = if (mOrientation == HORIZONTAL) 1000 else 100

    private val mDefaultViewHeight = if (mOrientation == HORIZONTAL) 100 else 1000

    var onColorChange: ((Int) -> Unit)? = null

    private val colorArrays = intArrayOf(
        Color.RED,          // 红
        Color.rgb(255,127,0), // 橙
        Color.YELLOW,       // 黄
        Color.GREEN,        // 绿
        Color.BLUE,         // 蓝
        Color.rgb(75,0,130),  // 靛
        Color.rgb(143,0,255),  // 紫
        Color.BLACK,
        Color.WHITE
    )

    private val mCirclePointerPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 4f
            color = Color.WHITE
        }
    }

    private val mViewPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            strokeWidth =
                if (mOrientation == HORIZONTAL) measuredHeight.toFloat() else measuredWidth.toFloat()
            shader = mColorLinearGradient
            strokeCap = Paint.Cap.ROUND
        }
    }

    private val mColorLinearGradient by lazy {
        if (mOrientation == HORIZONTAL) {
            LinearGradient(
                0f, 0f, measuredWidth.toFloat(), 0f,
                colorArrays,
                null,
                Shader.TileMode.CLAMP
            )
        } else {
            LinearGradient(
                0f, 0f, 0f, measuredHeight.toFloat(),
                colorArrays,
                null,
                Shader.TileMode.CLAMP
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val viewW = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.UNSPECIFIED, MeasureSpec.AT_MOST -> {
                mDefaultViewWidth
            }

            MeasureSpec.EXACTLY -> {
                MeasureSpec.getSize(widthMeasureSpec)
            }

            else -> {
                mDefaultViewHeight
            }
        }

        val viewH = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.UNSPECIFIED, MeasureSpec.AT_MOST -> {
                mDefaultViewHeight
            }

            MeasureSpec.EXACTLY -> {
                MeasureSpec.getSize(heightMeasureSpec)
            }

            else -> {
                mDefaultViewHeight
            }
        }

        setMeasuredDimension(viewW, viewH)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (mOrientation == HORIZONTAL) {
            val yOffset = (measuredHeight / 2).toFloat()
            canvas?.drawLine(yOffset, yOffset, measuredWidth - yOffset, yOffset, mViewPaint)

            canvas?.drawCircle(
                mPointerX, (measuredHeight / 2).toFloat(),
                (measuredHeight / 2).toFloat() - 2, mCirclePointerPaint
            )
        } else {
            val xOffset = (measuredWidth / 2).toFloat()
            canvas?.drawLine(xOffset, xOffset, xOffset, measuredHeight - xOffset, mViewPaint)
            canvas?.drawCircle(xOffset, mPointerY, xOffset - 2, mCirclePointerPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                if (mOrientation == HORIZONTAL) {
                    mPointerX = event.x
                    parseColor(event.x)
                } else {
                    mPointerY = event.y
                    parseColor(event.y)
                }
                invalidate()
            }
        }
        return true
    }

    private fun parseColor(f: Float) {
        val p = if (mOrientation == HORIZONTAL) {
            f / measuredWidth
        } else {
            f / measuredHeight
        }
        val color = getColorAtPosition(p)
        onColorChange?.invoke(color)
    }

    // 获取指定位置的颜色（x 是 0-1 之间的比例值）
    fun getColorAtPosition(x: Float): Int {
        if (colorArrays.size == 1) return colorArrays[0]
        if (x <= 0) return colorArrays[0]
        if (x >= 1) return colorArrays.last()

        // 计算颜色索引和插值比例
        val position = x * (colorArrays.size - 1)
        val index = position.toInt()
        val fraction = position - index

        return if (index < colorArrays.size - 1) {
            interpolateColor(colorArrays[index], colorArrays[index + 1], fraction)
        } else {
            colorArrays.last()
        }
    }

    private fun interpolateColor(color1: Int, color2: Int, fraction: Float): Int {
        val alpha =
            (Color.alpha(color1) + fraction * (Color.alpha(color2) - Color.alpha(color1))).toInt()
        val red = (Color.red(color1) + fraction * (Color.red(color2) - Color.red(color1))).toInt()
        val green =
            (Color.green(color1) + fraction * (Color.green(color2) - Color.green(color1))).toInt()
        val blue =
            (Color.blue(color1) + fraction * (Color.blue(color2) - Color.blue(color1))).toInt()
        return Color.argb(alpha, red, green, blue)
    }

//    fun getPositionFromColorByMutliColor(
////        colors: IntArray,       // 渐变颜色数组
//        positions: FloatArray,  // 颜色对应的位置数组
//        targetColor: Int
//    ): Float {
//        val targetR = Color.red(targetColor)
//        val targetG = Color.green(targetColor)
//        val targetB = Color.blue(targetColor)
//
//        // 找到目标颜色所在的区间
//        for (i in 0 until colorArrays.size - 1) {
//            val startColor = colorArrays[i]
//            val endColor = colorArrays[i + 1]
//            val startPos = positions[i]
//            val endPos = positions[i + 1]
//
//            // 检查目标颜色是否在当前区间内（简化判断，实际应使用更精确的方法）
//            val startR = Color.red(startColor)
//            val endR = Color.red(endColor)
//            val inRRange = if (startR <= endR)
//                targetR in startR..endR
//            else
//                targetR in endR..startR
//
//            if (inRRange) {
//                // 计算在当前区间内的比例
//                val localPosition = getPositionFromColor(startColor, endColor, targetColor)
//                // 转换为全局位置
//                return startPos + localPosition * (endPos - startPos)
//            }
//        }
//
//        return -1f // 未找到匹配的颜色区间
//    }

    fun getPositionFromColor(
        startColor: Int,
        endColor: Int,
        targetColor: Int
    ): Float {
        // 分解颜色为 RGBA 分量
        val startR = Color.red(startColor)
        val startG = Color.green(startColor)
        val startB = Color.blue(startColor)

        val endR = Color.red(endColor)
        val endG = Color.green(endColor)
        val endB = Color.blue(endColor)

        val targetR = Color.red(targetColor)
        val targetG = Color.green(targetColor)
        val targetB = Color.blue(targetColor)

        // 分别计算 R、G、B 通道的位置比例
        val rPosition = if (endR != startR) (targetR - startR).toFloat() / (endR - startR) else 0f
        val gPosition = if (endG != startG) (targetG - startG).toFloat() / (endG - startG) else 0f
        val bPosition = if (endB != startB) (targetB - startB).toFloat() / (endB - startB) else 0f

        // 返回三个通道的平均值（更精确）
        return (rPosition + gPosition + bPosition) / 3f
    }
}
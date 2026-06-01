package com.fizzer.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class CircleRotateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    //最大的旋转角度值
    private val MAX_ANGLE_VALUE = 80f

    //最小的旋转角度值
    private val MIN_ANGLE_VALUE = -80f

    //旋转的角度范围
    private val ANGLE_VALUE_RANGE = MAX_ANGLE_VALUE - MIN_ANGLE_VALUE

    private var currentAngle = 0f // 当前轮盘角度

    private var lastTouchAngle = 0f

    //锚点
    private val mAnchorPoint = Point()

    private val mViewRect = RectF()

    //绘制弧形的半径
    private var mRadius = 0f

    private var isDragging = false

    // 角度变化监听器
    var onAngleChangedListener: ((angle: Float) -> Unit)? = null

    private val mCanvasPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
        strokeWidth = 4f
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 28f
        textAlign = Paint.Align.CENTER
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val viewW = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> {
                MeasureSpec.getSize(widthMeasureSpec)
            }

            else -> {
                300
            }
        }

        val viewH = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> {
                MeasureSpec.getSize(heightMeasureSpec)
            }

            else -> {
                200
            }
        }

        setMeasuredDimension(viewW, viewH)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //设置中心锚点
        mAnchorPoint.set(w / 2, 0)
        //设置弧形半径
        mRadius = Math.min(w.toFloat(), h.toFloat() * 2) * 0.5f - 10
        mViewRect.set(
            mAnchorPoint.x - mRadius,
            mAnchorPoint.y - mRadius,
            mAnchorPoint.x + mRadius,
            mAnchorPoint.y + mRadius
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas?.save()

        // 旋转整个画布（使整个轮盘旋转）
        canvas?.rotate(currentAngle, mAnchorPoint.x.toFloat(), mAnchorPoint.y.toFloat())
        canvas?.drawCircle(mAnchorPoint.x.toFloat(), mAnchorPoint.y.toFloat(), 5f, mCanvasPaint)
//        canvas?.drawArc(mViewRect, 0f, 180f, true, mCanvasPaint)

        drawMark(canvas)

        canvas?.restore()
    }


    /**
     * 绘制刻度
     */
    private fun drawMark(canvas: Canvas?) {

        // 绘制刻度和文字
        for (index in MAX_ANGLE_VALUE.toInt() downTo MIN_ANGLE_VALUE.toInt() step 2) {
            // 计算刻度位置（从0度开始，按比例分布）
            val angle = (-(index - MAX_ANGLE_VALUE)) / ANGLE_VALUE_RANGE * 180f
//            Log.e("Fizzer", "angle = $angle i = $index")
            val rad = Math.toRadians(angle.toDouble()).toFloat()
            // 计算刻度线的起点和终点
            val startX = mAnchorPoint.x + mRadius * cos(rad)
            val startY = mAnchorPoint.y + mRadius * sin(rad)

            if (index % 10 != 0) {
                canvas?.drawCircle(startX, startY, 2f, textPaint)
            } else {
                canvas?.drawCircle(startX, startY, 6f, textPaint)
            }

            // 每10度绘制一个数字
            if (index % 10 == 0) {
                // 计算文字位置
                val textX = mAnchorPoint.x + (mRadius - 50) * cos(rad)
                val textY = mAnchorPoint.y + (mRadius - 50) * sin(rad) + 10

                // 保存画布状态以绘制不旋转的文字
                canvas?.save()
                // 将文字位置的旋转抵消
                canvas?.rotate(-currentAngle, textX, textY)
                canvas?.drawText(index.toString(), textX, textY, textPaint)
                canvas?.restore()
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                if (isPointOnDial(x, y)) {
                    isDragging = true
                    lastTouchAngle = calculateAngle(x, y)
                    return true
                }
            }

            MotionEvent.ACTION_MOVE->{

                if(isDragging){
                    //计算当下滑动的角度
                    val touchAngle = calculateAngle(x, y)
                    //计算此时的角度与按下的角度差
                    val angleDelta = touchAngle - lastTouchAngle
                    // 更新当前角度（限制在范围内）
                    currentAngle = (currentAngle + angleDelta).coerceIn(-90f, 90f)

                    // 通知监听器
                    onAngleChangedListener?.invoke(currentAngle * MAX_ANGLE_VALUE / 90)
                    lastTouchAngle = touchAngle
                    invalidate()
                    return true
                }
            }

            MotionEvent.ACTION_UP->{
                isDragging = false
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    // 计算触摸点相对于中心点的角度（0-360度）
    private fun calculateAngle(x: Float, y: Float): Float {
        val dx = x - mAnchorPoint.x
        val dy = y - mAnchorPoint.y
        var angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
        if (angle < 0) angle += 360
        return angle
    }

    // 判断触摸点是否在圆盘上
    private fun isPointOnDial(x: Float, y: Float): Boolean {
        val dx = x - mAnchorPoint.x
        val dy = y - mAnchorPoint.y
        val distance = sqrt(dx * dx + dy * dy)
        return distance <= mRadius
    }
}
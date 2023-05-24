package com.fizzer.widget.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.fizzer.widget.R

/**
 * @Author:Fizzer
 * @Email: fizzer503@gmail.com
 * @Date: 2023/5/23
 * @Description: 圆形进度条自定义View
 */
class CircleProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    //开始角度
    private var mStartAngle = -90

    //结束角度
    private var mEndAngle = 360

    //当前角度
    private var mCurrentAngle = 0f

    //圆形半径
    private var mRadius: Int = 0

    //圆环宽度
    private var mStrokeWidth = 0

    //圆环背景色
    private var mProgressBarBgColor = 0

    //当前进度圆环的颜色
    private var mProgressBarColor = 0

    //最大进度
    private var mMaxProgress = 100f

    //当前进度
    private var mCurrentProgress = 0f

    //进度显示文案
    private var mTextStr = ""

    //文案颜色
    private var mTextColor = 0

    //文案大小
    private var mTextSize = 0

    //是否需要加载动画
    private var isAnimation = false

    //动画执行时间
    private var mAnimatorDuration = 1000

    init {
        var ta = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar)
        ta.apply {
            mRadius = getDimensionPixelSize(R.styleable.CircleProgressBar_cpb_radius, 50)
            mStrokeWidth = getDimensionPixelSize(R.styleable.CircleProgressBar_cpb_stroke_width, 8)
            mProgressBarBgColor = getColor(
                R.styleable.CircleProgressBar_cpb_progress_bar_bg_color,
                ContextCompat.getColor(context, R.color.black)
            )
            mProgressBarColor = getColor(
                R.styleable.CircleProgressBar_cpb_progress_bar_color,
                ContextCompat.getColor(context, R.color.white)
            )
            mMaxProgress = getFloat(R.styleable.CircleProgressBar_cpb_max_progress, 100f)
            mCurrentProgress = getFloat(R.styleable.CircleProgressBar_cpb_current_progress, 0f)
            mTextStr = getString(R.styleable.CircleProgressBar_cpb_text) ?: ""
            mTextColor = getColor(
                R.styleable.CircleProgressBar_cpb_text_color,
                ContextCompat.getColor(context, R.color.black)
            )
            mTextSize = getDimensionPixelSize(R.styleable.CircleProgressBar_cpb_text_size, 14)
        }
        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width = 0
        when (MeasureSpec.getMode(widthMeasureSpec)) {
            //如果未指定宽高，那么就以半径来确定宽高
            MeasureSpec.UNSPECIFIED, MeasureSpec.AT_MOST -> {
                width = mRadius * 2
            }
            MeasureSpec.EXACTLY -> {
                width = MeasureSpec.getSize(widthMeasureSpec)
            }
        }
        setMeasuredDimension(width, width)
    }

    override fun onDraw(canvas: Canvas?) {
        var centerX = width / 2
        var rectF = RectF()
        rectF.left = mStrokeWidth.toFloat()
        rectF.top = mStrokeWidth.toFloat()
        rectF.right = (width - mStrokeWidth).toFloat()
        rectF.bottom = (width - mStrokeWidth).toFloat()

        //绘制进度条背景
        drawProgressBarBg(canvas, rectF)
        //绘制进度
        drawProgress(canvas, rectF)
        //绘制中心文本
        drawCenterText(canvas, centerX)
    }

    /**
     * 绘制进度条背景
     */
    private fun drawProgressBarBg(canvas: Canvas?, rectF: RectF) {
        var mPaint = Paint()
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mStrokeWidth.toFloat()
        mPaint.isAntiAlias = true
        mPaint.color = mProgressBarBgColor
        mPaint.strokeCap = Paint.Cap.ROUND
        canvas?.drawArc(rectF, mStartAngle.toFloat(), mEndAngle.toFloat(), false, mPaint)
    }

    /**
     * 绘制当前进度
     */
    private fun drawProgress(canvas: Canvas?, rectF: RectF) {
        var mPaint = Paint()
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mStrokeWidth.toFloat()
        mPaint.color = mProgressBarColor
        mPaint.isAntiAlias = true
        mPaint.strokeCap = Paint.Cap.ROUND
        if (!isAnimation) {
            mCurrentAngle = 360 * (mCurrentProgress / mMaxProgress)
        }
        canvas?.drawArc(rectF, mStartAngle.toFloat(), mCurrentAngle.toFloat(), false, mPaint)
    }

    /**
     * 绘制中心文字
     */
    private fun drawCenterText(canvas: Canvas?, centerX: Int) {
        var mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = mTextColor
        mPaint.textAlign = Paint.Align.CENTER
        mPaint.textSize = mTextSize.toFloat()
        var textBound = Rect()
        mPaint.getTextBounds(mTextStr, 0, mTextStr.length, textBound)
        canvas?.drawText(
            mTextStr,
            centerX.toFloat(), (textBound.height() / 2 + height / 2).toFloat(), mPaint
        )
    }

    /**
     * 设置动画
     */
    private fun setAnimation(start: Float, target: Float) {
        isAnimation = true
        var animator = ValueAnimator.ofFloat(start, target)
        animator.duration = mAnimatorDuration.toLong()
        animator.setTarget(mCurrentAngle)
        animator.addUpdateListener {
            mCurrentAngle = it.animatedValue as Float
            invalidate()
        }
        animator.start()
    }

    /**
     * 设置进度
     */
    fun setProgress(progress: Float) {
        if (progress < 0) {
            throw java.lang.IllegalArgumentException("Progress can not be less than 0")
        }
        //如果设置的进度一样，这里直接返回，降低性能消耗
        if (progress == mCurrentProgress) {
            return
        }
        var mLastAngle = 360 * (mCurrentProgress / mMaxProgress)
        mCurrentProgress = if (progress > mMaxProgress) {
            mMaxProgress
        } else {
            progress
        }

        mCurrentAngle = 360 * (mCurrentProgress / mMaxProgress)
        setAnimation(mLastAngle, mCurrentAngle)
    }

    /**
     * 设置中心文本
     */
    fun setText(text: String) {
        mTextStr = text
    }

    /**
     * 设置文本颜色
     */
    fun setTextColor(color: Int) {
        if (color <= 0) {
            throw IllegalArgumentException("Color is can not be less than 0")
        }
        mTextColor = color
    }

    /**
     * 设置文本字号
     */
    fun setTextSize(textSize: Float) {
        if (textSize <= 0) {
            throw IllegalArgumentException("text size can not be less than 0")
        }
        mTextSize = textSize.toInt()
    }
}
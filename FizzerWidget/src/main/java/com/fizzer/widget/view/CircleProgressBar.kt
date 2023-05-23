package com.fizzer.widget.view

import android.content.Context
import android.util.AttributeSet
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

    //圆形半径
    private var mRadius: Int = 0

    //圆环宽度
    private var mStrokeWidth = 0

    //圆环背景色
    private var mProgressBarBgColor = 0

    //当前进度圆环的颜色
    private var mProgressBarColor = 0

    //最大进度
    private var mMaxProgress = 100

    //当前进度
    private var mCurrentProgress = 0

    //进度显示文案
    private var mTextStr = ""

    //文案颜色
    private var mTextColor = 0

    //文案大小
    private var mTextSize = 0

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
            mMaxProgress = getInt(R.styleable.CircleProgressBar_cpb_max_progress, 100)
            mCurrentProgress = getInt(R.styleable.CircleProgressBar_cpb_current_progress, 0)
            mTextStr = getString(R.styleable.CircleProgressBar_cpb_text) ?: ""
            mTextColor = getColor(
                R.styleable.CircleProgressBar_cpb_text_color,
                ContextCompat.getColor(context, R.color.black)
            )
            mTextSize = getDimensionPixelSize(R.styleable.CircleProgressBar_cpb_text_size, 14)
        }
        ta.recycle()
    }
}
package com.fizzer.widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
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

    private var mRadius: Int = 0

    init {
        var ta = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar)
    }
}
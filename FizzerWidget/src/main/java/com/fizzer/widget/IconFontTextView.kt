package com.fizzer.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
* @Author: Fizzer
* @Email: fizzer503@sina.com
* @Date: 2022/11/18
* @Descriptor: IconFont 自定义的组件
*/
class IconFontTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    var iconFontPath = "iconfont.ttf"

    init {
        initAttr(context)
    }

    private fun initAttr(context: Context) {
        val tf = Typeface.createFromAsset(context.assets, iconFontPath)
        typeface = tf
    }

}
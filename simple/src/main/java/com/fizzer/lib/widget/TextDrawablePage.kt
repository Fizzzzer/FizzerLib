package com.fizzer.lib.widget

import android.graphics.Color
import android.graphics.Rect
import android.util.TypedValue
import com.fizzer.base.lib.android.act.BaseVBActivity
import com.fizzer.lib.databinding.ActivityTextDrawableBinding
import com.fizzer.widget.view.TextDrawable

class TextDrawablePage : BaseVBActivity<ActivityTextDrawableBinding>() {
    override fun bindingInflate() = ActivityTextDrawableBinding.inflate(layoutInflater)

    override fun initView() {

        val textWrapDrawable = TextDrawable().apply {
            setTextView(binding.text)
            backgroundColor = Color.RED // 设置背景色
        }

// 将Drawable设置为TextView的背景
        binding.text.background = textWrapDrawable
//        binding.text.setBackgroundColor(Color.YELLOW)
    }

    override fun initEvent() {
    }

    override fun lazyInitData() {
    }
}
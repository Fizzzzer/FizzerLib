package com.fizzer.lib.widget

import androidx.appcompat.app.AppCompatActivity
import com.fizzer.base.lib.android.act.BaseVBActivity
import com.fizzer.lib.databinding.ActivityStrokedTextViewBinding

class StrokedTestViewPage: BaseVBActivity<ActivityStrokedTextViewBinding>() {
    override fun bindingInflate()= ActivityStrokedTextViewBinding.inflate(layoutInflater)

    override fun initView() {
    }

    override fun initEvent() {
    }

    override fun lazyInitData() {
    }
}
package com.fizzer.lib.widget

import android.util.Log
import com.fizzer.base.lib.android.act.BaseVBActivity
import com.fizzer.lib.databinding.ActivityRotateMarkLayoutBinding

class RotateMarkPage : BaseVBActivity<ActivityRotateMarkLayoutBinding>() {
    override fun bindingInflate() = ActivityRotateMarkLayoutBinding.inflate(layoutInflater)

    override fun initView() {
        binding.roteView.onAngleChangedListener = {
            Log.e("Fizzer","旋转角度 = $it")
            binding.testImg.rotation = it
        }
    }

    override fun initEvent() {

    }

    override fun lazyInitData() {

    }
}
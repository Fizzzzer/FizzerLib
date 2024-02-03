package com.fizzer.base.lib.android.dialog

import android.view.Gravity
import androidx.viewbinding.ViewBinding
import com.fizzer.base.lib.R
import com.fizzer.base.lib.android.BaseViewModel
import com.fizzer.base.lib.utils.UIUtils

/**
 * @Author fizzer
 * @Data 2021/7/18 - 11:24 上午
 * @Email Fizzer53@sina.com
 * @Describe:从底部弹出的对话框，这里对这个底部的对话框做一个单独的封装
 * 主要就是修改了显示位置和弹出的动画
 */
abstract class BaseBottomDialog<VB : ViewBinding, T : BaseViewModel> : BaseDialog<VB, T>() {

    override fun onStart() {
        super.onStart()
        var attr = dialog?.window?.attributes
        attr?.apply {
            gravity = Gravity.BOTTOM
        }
        dialog?.window?.attributes = attr
        dialog?.window?.setWindowAnimations(R.style.bottom_dialog_anim)
    }

    override fun getDialogWidth() = UIUtils.getWindowWidth(activity)

    override fun getBackgroundDrawableRes() = R.drawable.lib_bottom_dialog_bg

    override fun canceledOnTouchOutside() = true
}
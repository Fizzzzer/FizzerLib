package com.fizzer.base.lib.android

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.DrawableRes
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.fizzer.base.lib.R
import com.fizzer.base.lib.utils.UIUtils

/**
 * @Author fizzer
 * @Data 2021/7/18 - 10:03 上午
 * @Email Fizzer53@sina.com
 * @Describe:
 * Dialog的基类，这里主要是用来处理Dialog的事件
 */
abstract class BaseDialog<VB : ViewBinding, T : BaseViewModel> : DialogFragment() {
    private lateinit var root: View
    lateinit var mViewBinding: VB
    var mViewModel: T? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mViewBinding = inflateViewBinding(inflater)
        return mViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getViewModelClass()?.let {
            mViewModel = ViewModelProvider(this).get(it)
        }
    }

    override fun onStart() {
        super.onStart()
        var attr = dialog?.window?.attributes
        attr?.apply {
            width = getDialogWidth()
        }
        dialog?.window?.attributes = attr
        //设置默认背景透明，不然可能自己设置的背景被遮挡没效果
        dialog?.window?.setBackgroundDrawableResource(getBackgroundDrawableRes())
        //设置外面是否可点击
        dialog?.setCanceledOnTouchOutside(canceledOnTouchOutside())

        initView()
        initEvent()
        initData()
    }

    /**
     * 获取根布局
     */
    fun getRootView(): View {
        return root
    }

    /**
     * 设置对话框的宽度，默认为屏幕宽度的85%
     */
    open fun getDialogWidth() = (UIUtils.getWindowWidth(activity) * 0.85).toInt()

    @DrawableRes
    open fun getBackgroundDrawableRes(): Int = R.drawable.lib_comm_dialog_bg

    //设置外面是否可点击对话框消失
    open fun canceledOnTouchOutside(): Boolean = false

    abstract fun inflateViewBinding(inflater: LayoutInflater): VB

    abstract fun getViewModelClass(): Class<T>?

    abstract fun initView()

    abstract fun initData()

    abstract fun initEvent()

}
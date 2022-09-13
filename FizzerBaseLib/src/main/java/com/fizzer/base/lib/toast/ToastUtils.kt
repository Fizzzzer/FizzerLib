package com.fizzer.base.lib.toast

import android.content.Context

/**
 * @Author:Fizzer
 * @Date:2021/9/17
 * @Email:Fizzer@miaoshitech.com
 * @Description:吐司工具类
 */
class ToastUtils private constructor() {

    private val mToastImpl by lazy { ToastImpl() }

    companion object {
        fun getInstance(): ToastUtils {
            return Holder.INSTANCE
        }
    }

    private object Holder {
        val INSTANCE = ToastUtils()
    }

    fun showToast(context: Context?, msg: String?) {
        mToastImpl.showShortToast(context, msg)
    }
}
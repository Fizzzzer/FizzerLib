package com.fizzer.base.lib.toast

import android.content.Context
import android.widget.Toast

/**
 * @Author:Fizzer
 * @Date:2021/9/17
 * @Email:Fizzer@miaoshitech.com
 * @Description:系统吐司的实现类
 */
class ToastImpl : IShowToast {
    override fun showShortToast(context: Context?, msg: String?) {
        context?.let {
            Toast.makeText(context, msg ?: "", Toast.LENGTH_SHORT).show()
        }
    }

    override fun showLongToast(context: Context?, msg: String?) {
        context?.let {
            Toast.makeText(context, msg ?: "", Toast.LENGTH_LONG).show()
        }
    }

    override fun showToastByTime(context: Context?, time: Long, msg: String?) {
    }
}
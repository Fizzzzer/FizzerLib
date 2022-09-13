package com.fizzer.base.lib.toast

import android.content.Context

/**
 * @Author:Fizzer
 * @Date:2021/9/17
 * @Email:Fizzer@miaoshitech.com
 * @Description:Toast的顶层接口
 */
interface IShowToast {

    fun showShortToast(context: Context?,msg: String?)

    fun showLongToast(context: Context?,msg: String?)

    fun showToastByTime(context: Context?,time: Long = 0, msg: String?)
}
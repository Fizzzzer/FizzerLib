package com.fizzer.base.lib.utils

import android.content.Context
import android.os.Build
import android.util.TypedValue
import android.view.WindowManager

/**
 * @Author:Fizzer
 * @Email: fizzer503@gmail.com
 * @Date: 2022/9/13
 * @Description: UI像素相关的工具类
 */
object UIUtils {

    /**
     * DP 转成 PX像素
     */
    @JvmStatic
    fun dp2px(context: Context?, dpValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            context?.resources?.displayMetrics
        ).toInt()
    }

    /**
     * SP 转成 PX像素
     */
    @JvmStatic
    fun sp2px(context: Context?, spValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spValue,
            context?.resources?.displayMetrics
        ).toInt()
    }

    /**
     * 获取Windows的宽度
     */
    @JvmStatic
    fun getWindowWidth(context: Context?): Int {
        val wm = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            wm.currentWindowMetrics.bounds.width()
        } else {
            wm.defaultDisplay.width
        }
    }

    /**
     * 获取屏幕的高度
     */
    @JvmStatic
    fun getWindowHeight(context: Context?): Int {
        val wm = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            wm.currentWindowMetrics.bounds.height()
        } else {
            wm.defaultDisplay.height
        }
    }
}
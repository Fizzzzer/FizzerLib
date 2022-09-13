package com.fizzer.base.lib.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * @Author:Fizzer
 * @Email: fizzer503@gmail.com
 * @Date: 2022/9/13
 * @Description: App跳转工具类
 * 主要是跳转到APP的一些常用内置应用
 */
object AppJumpUtils {


    /**
     * 调用系统拨打电话
     */
    fun callPhone(context: Context?, phone: String?) {
        if (phone.isNullOrEmpty()) {
            return
        }
        val intent = Intent(Intent.ACTION_DIAL)
        val data = Uri.parse("tel:$phone")
        intent.data = data
        context?.startActivity(intent)
    }
}
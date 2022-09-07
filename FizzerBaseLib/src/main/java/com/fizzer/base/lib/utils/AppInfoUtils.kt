package com.fizzer.base.lib.utils

import android.content.Context

/**
 * @Author:Fizzer
 * @Email: fizzer503@gmail.com
 * @Date: 2022/9/7
 * @Descript: 应用信息的工具类
 */
object AppInfoUtils {

    /**
     * 获取当前APP的Version Name
     */
    fun getVersionName(context: Context?): String {
        context?.let {
            val pm = it.packageManager
            kotlin.runCatching {
                val appInfo = pm?.getPackageInfo(it.packageName, 0)
                return appInfo?.versionName ?: ""
            }.onFailure {
            }
        }
        return ""
    }
}
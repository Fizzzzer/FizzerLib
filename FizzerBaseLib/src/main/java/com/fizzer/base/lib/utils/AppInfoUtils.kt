package com.fizzer.base.lib.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.os.Build

/**
 * @Author:Fizzer
 * @Email: fizzer503@gmail.com
 * @Date: 2022/9/7
 * @Descriptor: 应用信息的工具类
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

    /**
     * 获取当前APP的版本号
     */
    fun getVersionCode(context: Context?): Long {
        context?.let {
            val pm = it.packageManager
            kotlin.runCatching {
                val appInfo = pm?.getPackageInfo(it.packageName, 0)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    return appInfo?.longVersionCode ?: 0
                } else {
                    return appInfo?.versionCode?.toLong() ?: 0
                }
            }
        }
        return 0
    }


    /**
     * 判断是否已安装了当前APP
     * @return true:已安装   false:未安装
     */
    fun isAppAvailable(context: Context?, packageName: String?): Boolean {
        if (packageName.isNullOrEmpty()) {
            return false
        }
        val pm = context?.packageManager
        val packageInfo = pm?.getInstalledPackages(0)
        packageInfo?.forEach {
            val pkgStr = it.packageName
            if (pkgStr.equals(packageName)) {
                return true
            }
        }
        return false
    }

}
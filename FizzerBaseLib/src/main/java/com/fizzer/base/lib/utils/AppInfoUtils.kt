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
    @JvmStatic
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
    @JvmStatic
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
    @JvmStatic
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

    /**
     * 判断当前的进程是否是APP的主进程
     */
    @JvmStatic
    fun isMainProcess(context: Context): Boolean {
        val pid = android.os.Process.myPid()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (appProcess in activityManager.runningAppProcesses) {
            if (appProcess.processName == context.packageName) {
                return appProcess.pid == pid
            }
        }
        return false
    }

    /**
     * 获取当前应用的包名
     */
    fun getPkgName(ctx: Context): String? {
        var pkgName = "Unknow"
        try {
            val manager = ctx.packageManager
            val info = manager.getPackageInfo(ctx.packageName, 0)
            pkgName = info.packageName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return pkgName
    }

}
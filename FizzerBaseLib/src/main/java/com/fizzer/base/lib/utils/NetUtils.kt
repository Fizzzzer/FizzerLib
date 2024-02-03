package com.fizzer.base.lib.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * @Author: Fizzer
 * @Email: fizzer503@sina.com
 * @Date: 2024/2/2
 * @Descriptor: 手机网络状态的工具类
 */
object NetUtils {
    /**
     * 获取当前网络是否已连接
     */
    fun isNetWorkConnected(context: Context?): Boolean {
        return context?.let {
            val connect = it.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val netWork = connect.activeNetwork
                val caption = connect.getNetworkCapabilities(netWork)
                caption?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false ||
                        caption?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
            } else {
                val netInfo = connect.activeNetworkInfo
                netInfo?.isConnectedOrConnecting
            }
            true
        } ?: false
    }
}
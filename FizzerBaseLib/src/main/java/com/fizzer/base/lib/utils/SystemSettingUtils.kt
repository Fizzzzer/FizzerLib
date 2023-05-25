package com.fizzer.base.lib.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.UserHandle
import android.provider.Settings


/**
 * @Author:Fizzer
 * @Email: fizzer503@gmail.com
 * @Date: 2023/5/25
 * @Description: 系统设置工具类
 */
object SystemSettingUtils {


    /**
     * 设置飞行模式
     * @param airPlaneMode true:开启飞行模式  false:关闭飞行模式
     */
    fun setAirPlaneMode(context: Context?, airPlaneMode: Boolean) {
        context?.let {
            //1-开启飞行模式   0-关闭飞行模式
            var airModeValue = if (airPlaneMode) 1 else 0
            Settings.Global.putInt(
                context.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON,
                airModeValue
            )
            val intent = Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            intent.putExtra("state", airPlaneMode)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.sendBroadcastAsUser(intent, UserHandle.getUserHandleForUid(-1))
            }
        }
    }

    /**
     * 获取当前设备的飞行模式开关
     * @return true:飞行模式开启  false:飞行模式关闭
     */
    fun isAirModeOn(context: Context): Boolean {
        return Settings.System.getInt(
            context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0
        ) == 1
    }
}
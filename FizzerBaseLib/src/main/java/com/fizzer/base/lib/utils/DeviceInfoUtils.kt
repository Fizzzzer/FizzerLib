package com.fizzer.base.lib.utils

import android.content.Context
import android.nfc.NfcManager
import android.os.Build

/**
 * @Author:Fizzer
 * @Email: fizzer503@gmail.com
 * @Date: 2022/9/7
 * @Descriptor: 获取当前设备信息的工具类
 */
object DeviceInfoUtils {

    /**
     * 获取厂商名字
     */
    fun getDeviceManufacturer(): String {
        return Build.MANUFACTURER
    }

    /**
     * 获取产品名
     */
    fun getDeviceProduct(): String {
        return Build.PRODUCT
    }

    /**
     * 获取设备品牌
     */
    fun getDevicesBrandName(): String {
        return Build.BRAND
    }

    /**
     * 获取设备型号
     */
    fun getDeviceModel(): String {
        return Build.MODEL
    }

    /**
     * 获取设备的主板名
     */
    fun getDeviceBoard(): String {
        return Build.BOARD
    }

    /**
     * 获取设备名称
     */
    fun getDeviceName(): String {
        return Build.DEVICE
    }

    /**
     * 获取设备指纹信息
     */
    fun getDeviceFingerprint(): String {
        return Build.FINGERPRINT
    }

    /**
     * 设备是否支持NFC功能
     * true - 支持
     * false - 不支持
     */
    fun hasNfcSupport(context: Context?): Boolean {
        val nfcManager = context?.getSystemService(Context.NFC_SERVICE) as NfcManager
        val nfcAdapter = nfcManager.defaultAdapter
        return nfcAdapter != null
    }

    fun getBuildSDK():Int{
        return Build.VERSION.SDK_INT
    }

    fun getAndroidVersion():String{
        return Build.VERSION.RELEASE
    }
}
package com.fizzer.base.lib.utils.security.impl

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Base64


internal class Base64Impl : IEncoder, IDecoder {
    override fun encodeStr(str: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            encodeAndroidUp26(str)
        } else {
            encodeAndroidDown26(str)
        }
    }

    override fun decode(str: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            decodeAndroidUp26(str)
        }else{
            decodeAndroidDown26(str)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun encodeAndroidUp26(str: String): String {
        return Base64.getEncoder().encodeToString(str.toByteArray())
    }

    private fun encodeAndroidDown26(str: String): String {
        return android.util.Base64.encodeToString(str.toByteArray(), android.util.Base64.DEFAULT)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun decodeAndroidUp26(str: String): String {
        val result = Base64.getDecoder().decode(str)
        return String(result)
    }

    private fun decodeAndroidDown26(str: String): String {
        val result = android.util.Base64.decode(str, android.util.Base64.DEFAULT)
        return String(result)
    }


}
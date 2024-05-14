package com.fizzer.base.lib.utils.security.impl

import java.math.BigInteger
import java.security.MessageDigest

internal class MD5Impl:IEncoder,IDecoder {
    private val md5Instance = MessageDigest.getInstance("MD5")
    override fun encodeStr(str:String): String {
        val  digest = md5Instance.digest(str.toByteArray())
        return BigInteger(1,digest).toString(16).padStart(32,'0')
    }

    override fun decode(str: String): String {
        return "MD5算法不支持解密"
    }
}
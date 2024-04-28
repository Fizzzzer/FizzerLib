package com.fizzer.base.lib.utils.security.impl

import java.security.MessageDigest

internal class SHA1Impl :IEncoder,IDecoder{
    override fun encodeStr(str:String): String {
        val digest = MessageDigest.getInstance("SHA-1")
        val bytes = digest.digest(str.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    override fun decode(str: String): String {
        return "SHA-1算法不支持解密"
    }
}
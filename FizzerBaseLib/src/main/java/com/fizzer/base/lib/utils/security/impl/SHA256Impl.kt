package com.fizzer.base.lib.utils.security.impl

import java.security.MessageDigest

internal class SHA256Impl:IEncoder,IDecoder {
    override fun encodeStr(str:String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(str.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    override fun decode(str: String): String {
        return "SHA-256算法不支持解密"
    }
}
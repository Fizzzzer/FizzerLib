package com.fizzer.base.lib.utils.security.impl

import com.fizzer.base.lib.utils.security.SecurityImpl
import java.io.File
import java.io.FileInputStream
import java.lang.RuntimeException
import java.security.MessageDigest

internal class MD5Security : SecurityImpl {
    override fun encode(string: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(string.toByteArray())
        return digest.joinToString("") {
            String.format("%02x", it)
        }
    }

    override fun decode(string: String): String {
        throw RuntimeException("MD5不支持解密行为")
    }

    override fun encodeFile(file: File?, encodeFile: File): String {
        val md = MessageDigest.getInstance("MD5")
        val buffer = ByteArray(8192)
        FileInputStream(file).use {
            var bytesRead = it.read(buffer)
            while (bytesRead != -1) {
                md.update(buffer, 0, bytesRead)
                bytesRead = it.read(buffer)
            }
        }
        val digest = md.digest()
        return digest.joinToString("") {
            String.format("%02x", it)
        }
    }

    override fun decodeFile(encodeFile: File, decodeFile: File) {
        throw RuntimeException("MD5不支持解密行为")
    }
}
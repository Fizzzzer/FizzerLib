package com.fizzer.base.lib.utils.security

import java.io.File

interface SecurityImpl {
    /**
     * 加密字符串
     */
    fun encode(string: String): String

    /**
     * 解密字符串
     */
    fun decode(string: String): String

    /**
     * 加密文件
     */
    fun encodeFile(file: File?, encodeFile: File): String

    /**
     * 解密文件
     */
    fun decodeFile(encodeFile: File, decodeFile: File)
}
package com.fizzer.base.lib.utils.security.impl

interface IEncoder {
    /**
     * 加密字符串
     */
    fun encodeStr(str:String):String
}
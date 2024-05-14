package com.fizzer.base.lib.utils.security

import com.fizzer.base.lib.utils.security.impl.Base64Impl
import com.fizzer.base.lib.utils.security.impl.IDecoder
import com.fizzer.base.lib.utils.security.impl.IEncoder
import com.fizzer.base.lib.utils.security.impl.MD5Impl
import com.fizzer.base.lib.utils.security.impl.SHA1Impl
import com.fizzer.base.lib.utils.security.impl.SHA256Impl

object SecurityUtils {
    private val mMD5Instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { MD5Impl() }
    private val mBase64Instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { Base64Impl() }
    private val mSHA1Instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { SHA1Impl() }
    private val mSHA256Instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { SHA256Impl() }

    fun getEncoder(type: SecurityType): IEncoder {

        return when (type) {
            SecurityType.MD5 -> mMD5Instance
            SecurityType.BASE64 -> mBase64Instance
            SecurityType.SHA1 -> mSHA1Instance
            SecurityType.SHA256 -> mSHA256Instance
        }
    }

    fun getDecoder(type: SecurityType):IDecoder{
        return when(type){
            SecurityType.MD5 -> mMD5Instance
            SecurityType.BASE64 -> mBase64Instance
            SecurityType.SHA1 -> mSHA1Instance
            SecurityType.SHA256 -> mSHA256Instance
        }
    }
}
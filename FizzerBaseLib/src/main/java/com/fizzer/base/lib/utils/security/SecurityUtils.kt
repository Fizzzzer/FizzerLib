package com.fizzer.base.lib.utils.security

import com.fizzer.base.lib.utils.security.impl.MD5Security

object SecurityUtils {

    fun getSecurityEngine(type: SecurityType): SecurityImpl? {
        return when (type) {
            SecurityType.MD5 -> return MD5Security()
            else -> null
        }
    }
}
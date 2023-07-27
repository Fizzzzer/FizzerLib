package com.fizzer.base.lib.log

object FizzerLog {
    init {
        LogUtils.INSTANCE.init()
    }

    fun error(any: Any?) {
        when (any) {
            is Throwable -> {
                LogUtils.INSTANCE.loge(any.localizedMessage ?: "Throwable is null")
            }

            else -> {
                LogUtils.INSTANCE.loge(any.toString())
            }
        }
    }

    fun debug(any: Any?) {
        when (any) {
            is Throwable -> {
                LogUtils.INSTANCE.logd(any.localizedMessage ?: "Throwable is null")
            }

            else -> {
                LogUtils.INSTANCE.logd(any.toString())
            }
        }
    }
}
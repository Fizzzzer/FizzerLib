package com.fizzer.base.lib.log

import android.util.Log
import com.fizzer.base.lib.log.ILog

/**
 * @Author fizzer
 * @Data 2022/1/22 - 7:16 下午
 * @Email Fizzer53@sina.com
 * @Describe:系统的日志方式
 */
internal class SystemLog : ILog {
    override fun loge(tag: String, msg: String) {
        Log.e(tag, msg)
    }

    override fun logi(tag: String, msg: String) {
        Log.i(tag, msg)
    }

    override fun logd(tag: String, msg: String) {
        Log.d(tag, msg)
    }
}
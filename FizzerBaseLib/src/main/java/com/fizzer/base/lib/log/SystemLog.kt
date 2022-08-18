package com.fizzer.fizzer_base.log

import android.util.Log

/**
 * @Author fizzer
 * @Data 2022/1/22 - 7:16 下午
 * @Email Fizzer53@sina.com
 * @Describe:系统的日志方式
 */
class SystemLog : ILog {
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
package com.fizzer.base.lib.log

/**
 * @Author fizzer
 * @Data 2022/1/22 - 7:13 下午
 * @Email Fizzer53@sina.com
 * @Describe:日志封装的接口
 */
internal interface ILog {
    fun loge(tag: String, msg: String)

    fun logi(tag: String, msg: String)

    fun logd(tag: String, msg: String)
}
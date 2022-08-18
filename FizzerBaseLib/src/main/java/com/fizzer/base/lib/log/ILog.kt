package com.fizzer.fizzer_base.log

/**
 * @Author fizzer
 * @Data 2022/1/22 - 7:13 下午
 * @Email Fizzer53@sina.com
 * @Describe:日志封装的接口
 */
interface ILog {
    fun loge(tag: String, msg: String)

    fun logi(tag: String, msg: String)

    fun logd(tag: String, msg: String)
}
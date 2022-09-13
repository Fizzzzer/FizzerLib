package com.fizzer.base.lib.log

/**
 * Author:Fizzer
 * Date:2021/3/10
 * Email:Fizzer@miaoshitech.com
 * Description:Log工具类
 */
class LogUtils private constructor() {
    private var mLog: ILog? = null
    private var mPreTag: String = "Fizzer"


    init {
        mLog = LogFactory.INSTANCE.createLog(LOG_SYSTEM)
    }

    companion object {
        const val LOG_SYSTEM = "SYSTEM"
        const val LOG_CUSTOMER = "CUSTOMER"
        val INSTANCE: LogUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { LogUtils() }
    }

    private fun formatTag(tag: String = ""): String {
        return "${mPreTag}_$tag"
    }

    /**
     * 初始化Tag
     */
    fun init(type: String = LOG_SYSTEM, tag: String = "Fizzer") {
        mLog = LogFactory.INSTANCE.createLog(type)
        mPreTag = tag
    }

    fun loge(msg: String) {
        loge("", msg)
    }

    fun loge(tag: String, msg: String) {
        mLog?.loge(formatTag(tag = tag), msg)
    }

    fun logi(msg: String) {
        logi("", msg)
    }

    fun logi(tag: String, msg: String) {
        mLog?.logi(formatTag(tag = tag), msg)
    }

    fun logd(msg: String) {
        logd("", msg)
    }

    fun logd(tag: String, msg: String) {
        mLog?.logd(formatTag(tag = tag), msg)
    }

}
package com.fizzer.fizzer_base.log

/**
 * Author:Fizzer
 * Date:2021/3/10
 * Email:Fizzer@miaoshitech.com
 * Description:Log工具类
 */
class LogUtils private constructor() {
    private var mLog: ILog? = null
    private var mTag: String = "Fizzer"

    init {
        mLog = LogFactory.INSTANCE.createLog(LogFactory.LOG_SYSTEM)
    }

    companion object {
        val INSTANCE: LogUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { LogUtils() }
    }

    fun init(type: String = LogFactory.LOG_SYSTEM, tag: String = "Fizzer") {
        mLog = LogFactory.INSTANCE.createLog(type)
        mTag = tag
    }

    /**
     * 获取当前的log对象
     */
    fun getLogger(): ILog? = mLog


}
package com.fizzer.base.lib.log

/**
 * @Author fizzer
 * @Data 2022/1/22 - 7:17 下午
 * @Email Fizzer53@sina.com
 * @Describe:
 */
internal class LogFactory private constructor() {

    /**
     * Kotlin双重锁校验模式实现单例
     */
    companion object {
        val INSTANCE: LogFactory by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { LogFactory() }
    }

    /**
     * 创建对应的日志类
     */
    fun createLog(type: String): ILog {
        return when (type) {
            LogUtils.LOG_SYSTEM -> {
                SystemLog()
            }
            LogUtils.LOG_CUSTOMER -> {
                CustomerLog()
            }
            else -> {
                SystemLog()
            }
        }
    }

}
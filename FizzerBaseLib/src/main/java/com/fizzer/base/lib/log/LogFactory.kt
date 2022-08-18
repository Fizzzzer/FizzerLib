package com.fizzer.fizzer_base.log

/**
 * @Author fizzer
 * @Data 2022/1/22 - 7:17 下午
 * @Email Fizzer53@sina.com
 * @Describe:
 */
class LogFactory private constructor() {

    /**
     * Kotlin双重锁校验模式实现单例
     */
    companion object {
        const val LOG_SYSTEM = "SYSTEM"
        const val LOG_CUSTOMER = "CUSTOMER"

        val INSTANCE: LogFactory by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { LogFactory() }
    }

    /**
     * 创建对应的日志类
     */
    fun createLog(type: String): ILog {
        return when (type) {
            LOG_SYSTEM -> {
                SystemLog()
            }
            LOG_CUSTOMER -> {
                CustomerLog()
            }
            else -> {
                SystemLog()
            }
        }
    }

}
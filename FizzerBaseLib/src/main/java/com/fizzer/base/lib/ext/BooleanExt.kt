package com.fizzer.base.lib.ext

/**
 * @Author:Fizzer
 * @Email: fizzer503@gmail.com
 * @Date: 2023/7/27
 * @Description: Boolean的一些扩展方法
 */

sealed class BooleanExt<out T>
object OtherWise : BooleanExt<Nothing>()
class WithData<T>(val data: T) : BooleanExt<T>()

inline fun <T> Boolean.yes(block: () -> T): BooleanExt<T> {
    return when {
        this -> WithData(block())
        else -> OtherWise
    }
}

inline fun <T> BooleanExt<T>.otherwise(block: () -> T): T {
    return when (this) {
        is OtherWise -> block()
        is WithData -> this.data
    }
}

inline fun Boolean.no(block: () -> Unit) {
    if (this.not()) block()
}


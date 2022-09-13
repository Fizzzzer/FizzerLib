package com.fizzer.base.lib.sp

import java.io.Serializable

/**
 * @Author fizzer
 * @Data 2022/1/23 - 11:31 上午
 * @Email Fizzer53@sina.com
 * @Describe:
 */
interface ISharedPreferences {

    fun file(fileName: String?): ISharedPreferences

    fun putString(key: String, value: String)

    fun getString(key: String, defaultValue: String): String

    fun putInt(key: String, value: Int)

    fun getInt(key: String, defaultValue: Int): Int

    fun putBoolean(key: String, value: Boolean)

    fun getBoolean(key: String, defaultValue: Boolean): Boolean

    fun putFloat(key: String, value: Float)

    fun getFloat(key: String, defaultValue: Float): Float

    fun putLong(key: String, value: Long)

    fun getLong(key: String, defaultValue: Long): Long

    fun removeKey(key: String)

    fun clear()

    fun <T : Serializable> putList(key: String, value: List<T>?)

    fun <T : Serializable> getList(key: String): List<T>?

    fun <T : Serializable> putObject(key: String, obj: T?)

    fun <T : Serializable> getObject(key: String, obj: Class<T>): T?
}
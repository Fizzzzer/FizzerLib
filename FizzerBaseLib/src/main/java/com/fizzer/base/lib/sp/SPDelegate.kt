package com.fizzer.base.lib.sp

import android.content.Context
import java.io.Serializable
import java.lang.IllegalArgumentException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
class SPDelegate<T>(
    private var key: String, private var defaultValue: T, var fileName: String = ""
) : ReadWriteProperty<Any?, T> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findValue(key, defaultValue)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        saveValue(value)
    }

    private fun saveValue(value: T) {
        when (value) {
            is Int -> SpFactory.getSp(fileName).putInt(key = key, value = value)
            is Long -> SpFactory.getSp(fileName).putLong(key = key, value = value)
            is Boolean -> SpFactory.getSp(fileName).putBoolean(key = key, value = value)
            is String -> SpFactory.getSp(fileName).putString(key = key, value = value)
            is Float -> SpFactory.getSp(fileName).putFloat(key = key, value = value)
            is Serializable -> SpFactory.getSp(fileName).putObject(key = key, obj = value)
            else -> throw IllegalArgumentException("unSupport type Exception")
        }
    }

    private fun <T> findValue(valueKey: String, value: T): T {
        val res: Any = when (value) {
            is Int -> SpFactory.getSp(fileName).getInt(key = valueKey, defaultValue = value)
            is Long -> SpFactory.getSp(fileName).getLong(key = valueKey, defaultValue = value)
            is Boolean -> SpFactory.getSp(fileName).getBoolean(key = key, defaultValue = value)
            is String -> SpFactory.getSp(fileName).getString(key = key, defaultValue = value)
            is Float -> SpFactory.getSp(fileName).getFloat(key = key, defaultValue = value)
            is Serializable -> SpFactory.getSp(fileName).putObject(key = key, obj = value)
            else -> throw IllegalArgumentException("unSupport type Exception")
        }
        return res as T
    }
}
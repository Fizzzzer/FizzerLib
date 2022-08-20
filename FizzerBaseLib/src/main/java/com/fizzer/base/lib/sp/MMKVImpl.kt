package com.fizzer.base.lib.sp

import com.fizzer.fizzer_base.sp.ISharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import java.io.Serializable

internal class MMKVImpl : ISharedPreferences {

    private var kv = MMKV.defaultMMKV()


    override fun file(fileName: String?): ISharedPreferences {
        if (fileName?.isNotEmpty() == true) {
            kv = MMKV.mmkvWithID(fileName)
        }
        return this
    }

    override fun putString(key: String, value: String) {
        kv.encode(key, value)
    }

    override fun getString(key: String, defaultValue: String): String {
        return kv.decodeString(key, defaultValue) ?: ""
    }

    override fun putInt(key: String, value: Int) {
        kv.encode(key, value)
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return kv.decodeInt(key, defaultValue)
    }

    override fun putBoolean(key: String, value: Boolean) {
        kv.encode(key, value)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return kv.decodeBool(key, defaultValue)
    }

    override fun putFloat(key: String, value: Float) {
        kv.encode(key, value)
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return kv.decodeFloat(key, defaultValue)
    }

    override fun putLong(key: String, value: Long) {
        kv.encode(key, value)

    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return kv.decodeLong(key, defaultValue)
    }

    override fun removeKey(key: String) {
        kv.removeValueForKey(key)

    }

    override fun clear() {
        kv.clearAll()
    }

    override fun <T : Serializable> putList(key: String, value: List<T>?) {
        if (value?.isNullOrEmpty() == true) {
            return
        } else {
            val objString = Gson().toJson(value)
            kv.encode(key, objString)
        }
    }

    override fun <T : Serializable> getList(key: String): List<T>? {
        val objString = kv.decodeString(key)
        if (objString?.isNotEmpty() == true) {
            val type = object : TypeToken<List<T>>() {}.type
            return Gson().fromJson(objString, type)
        }
        return null
    }

    override fun <T : Serializable> putObject(key: String, obj: T?) {
        obj?.let {
            val objString = Gson().toJson(it)
            kv.encode(key, objString)
        }
    }

    override fun <T : Serializable> getObject(key: String, obj: Class<T>): T? {
        val objString = kv.decodeString(key)
        if (objString?.isNotEmpty() == true) {
            return Gson().fromJson(objString, obj)
        }
        return null
    }
}
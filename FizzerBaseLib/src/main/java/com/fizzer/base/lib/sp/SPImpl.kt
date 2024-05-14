package com.fizzer.base.lib.sp

import android.content.Context
import android.content.SharedPreferences
import com.fizzer.base.lib.ext.otherwise
import com.fizzer.base.lib.ext.yes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

/**
 * @Author fizzer
 * @Data 2022/1/23 - 11:42 上午
 * @Email Fizzer53@sina.com
 * @Description:SharedPreference的实现类
 */
internal class SPImpl(context: Context, private var defaultFileName: String) : ISharedPreferences {
    private var mContext: Context = context

    private var mSP: SharedPreferences? = null
    private var mEditor: SharedPreferences.Editor? = null

    init {
        mSP = mContext.getSharedPreferences(defaultFileName, Context.MODE_PRIVATE)
        mEditor = mSP?.edit()
    }

    override fun putString(key: String, value: String) {
        mEditor?.let {
            mEditor?.putString(key, value)
            commitData()
        } ?: let {
            throw RuntimeException("在使用之前,请先调用init()方法进行初始化")
        }
    }

    override fun getString(key: String, defaultValue: String): String {
        mSP?.let {
            return it.getString(key, defaultValue) ?: defaultValue
        } ?: let {
            return defaultValue
        }
    }

    override fun putInt(key: String, value: Int) {
        mEditor?.let {
            it.putInt(key, value)
            commitData()
        } ?: let {
            throw RuntimeException("在使用之前,请先调用init()方法进行初始化")
        }
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        mSP?.let {
            return it.getInt(key, defaultValue)
        } ?: let {
            throw RuntimeException("在使用之前,请先调用init()方法进行初始化")
        }
    }

    override fun putBoolean(key: String, value: Boolean) {
        mEditor?.let {
            it.putBoolean(key, value)
            commitData()
        } ?: let {
            throw RuntimeException("在使用之前,请先调用init()方法进行初始化")
        }
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        mSP?.let {
            return it.getBoolean(key, defaultValue)
        } ?: let {
            throw RuntimeException("在使用之前,请先调用init()方法进行初始化")
        }
    }

    override fun putFloat(key: String, value: Float) {
        mEditor?.let {
            it.putFloat(key, value)
            commitData()
        } ?: let {
            throw RuntimeException("在使用之前,请先调用init()方法进行初始化")
        }
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        mSP?.let {
            return it.getFloat(key, defaultValue)
        } ?: let {
            throw RuntimeException("在使用之前,请先调用init()方法进行初始化")
        }
    }

    override fun putLong(key: String, value: Long) {
        mEditor?.let {
            it.putLong(key, value)
            commitData()
        } ?: let {
            throw RuntimeException("在使用之前,请先调用init()方法进行初始化")
        }
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        mSP?.let {
            return it.getLong(key, defaultValue)
        } ?: let {
            throw RuntimeException("在使用之前,请先调用init()方法进行初始化")
        }
    }

    override fun removeKey(key: String) {
        mEditor?.remove(key) ?: let {
            throw RuntimeException("在使用之前,请先调用init()方法进行初始化")
        }
    }

    override fun clear() {
        mEditor?.clear()
    }

    /**
     * 在SP中存入List数据
     */
    override fun <T : Serializable> putList(key: String, value: List<T>?) {
        mEditor?.let {
            if (value?.isNullOrEmpty() == true) {
                return
            } else {
                val objString = Gson().toJson(value)
                it.putString(key, objString)
                commitData()
            }
        }
    }

    /**
     * 获取存入的List对象
     */
    override fun <T : Serializable> getList(key: String): List<T>? {
        mSP?.let {
            val objString = it.getString(key, "")
            if (objString?.isNotEmpty() == true) {
                val type = object : TypeToken<List<T>>() {}.type
                return Gson().fromJson(objString, type)
            }
        }
        return null
    }

    /**
     * 保存对象
     */
    override fun <T : Serializable> putObject(key: String, obj: T?) {
        mEditor?.let {
            obj?.let { obj ->
                val objString = Gson().toJson(obj)
                it.putString(key, objString)
                commitData()
            }
        } ?: let {
            throw RuntimeException("在使用之前,请先调用init()方法进行初始化")
        }

    }

    /**
     * 获取存入的obj对象
     */
    override fun <T : Serializable> getObject(key: String, obj: Class<T>): T? {
        mSP?.let {
            val objString = it.getString(key, "")
            if (objString?.isNotEmpty() == true) {
                return Gson().fromJson(objString, obj)
            }
        }
        return null
    }


    /**
     * 提交数据
     */
    private fun commitData() {
        mEditor?.apply()
    }
}
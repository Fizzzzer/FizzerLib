package com.fizzer.base.lib.sp

import android.content.Context
import com.fizzer.fizzer_base.sp.ISharedPreferences
import com.fizzer.fizzer_base.sp.SPImpl
import com.tencent.mmkv.MMKV

/**
 * @Author:Fizzer
 * @Email: qianqian.ma@tinno.com
 * @Date: 2022/8/18
 * @Descript: SP 工具类
 */
class SpUtils private constructor() {


    /**
     * 当前SP的类型
     */
    private var mSpType = SP_TYPE_SP

    /**
     * 获取当前的SP实例
     */
    val mSp: ISharedPreferences? get() = _mSp

    private var _mSp: ISharedPreferences? = null

    companion object {
        const val SP_TYPE_SP = "SP"
        const val SP_TYPE_MMKV = "MMKV"

        val INSTANCE: SpUtils by lazy { SpUtils() }
    }

    /**
     * 初始化
     * @param type 当前SP的类型，默认是系统提供的SharePreference
     * @param context 当前上下文
     * 最好在Application里面进行初始化
     */
    fun init(context: Context, type: String = SP_TYPE_SP) {
        mSpType = type

        when (type) {
            SP_TYPE_MMKV -> {
                MMKV.initialize(context)
                _mSp = MMKVImpl()
            }
            SP_TYPE_SP -> {
                _mSp = SPImpl(context)
            }
        }
    }


}
package com.fizzer.base.lib.sp

import android.content.Context
import com.tencent.mmkv.MMKV

/**
 * @Author:Fizzer
 * @Email: qianqian.ma@tinno.com
 * @Date: 2022/8/18
 * @Description: SP 工具类
 */
object SpFactory {

    const val SP_TYPE_SP = "SP"
    const val SP_TYPE_MMKV = "MMKV"
    private var _mSp: ISharedPreferences? = null

    /**
     * 初始化
     * @param type 当前SP的类型，默认是系统提供的SharePreference
     * @param context 当前上下文
     * 最好在Application里面进行初始化
     */
    fun init(context: Context, type: String = SP_TYPE_SP, fileName: String) {
        when (type) {
            SP_TYPE_MMKV -> {
                MMKV.initialize(context)
                _mSp = MMKVImpl()
            }

            SP_TYPE_SP -> {
                _mSp = SPImpl(context, fileName)
            }
        }
    }

    fun getSp(fileName: String?): ISharedPreferences {
        if (_mSp == null) {
            throw RuntimeException("Please invoke init method first!!!")
        }
        return _mSp!!.file(fileName)
    }


}
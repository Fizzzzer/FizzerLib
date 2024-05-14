package com.fizzer.base.lib.sp

import android.content.Context
import com.fizzer.base.lib.android.application.App
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

    //用来缓存当前的SP文件
    private var mSPMap = mutableMapOf<String, ISharedPreferences>()

    //默认的文件名称，在init的时候，进行指定，可以设置为全局的
    private var mDefaultFileName: String = "default"

    //指定的SP类型，默认是系统类型
    private var mSpType:String = SP_TYPE_SP


    /**
     * 初始化
     * @param type 当前SP的类型，默认是系统提供的SharePreference
     * @param context 当前上下文
     * 最好在Application里面进行初始化
     */
    fun init(context: Context, type: String = SP_TYPE_SP, fileName: String) {
        mDefaultFileName = fileName
        mSpType = type

        var mSp: ISharedPreferences? = null
        //这里做一下初始化操作
        when (type) {
            SP_TYPE_MMKV -> {
                MMKV.initialize(context)
            }
        }

        //实例化sp对象
        mSp = instanceSp(context,type,fileName)

        //加入到缓存列表中
        mSPMap[fileName] = mSp
    }

    fun getSp(fileName: String? = mDefaultFileName): ISharedPreferences {

        //防止传入的fileName为null的情况
        var mFileName = fileName
        if(fileName.isNullOrEmpty()){
            mFileName = mDefaultFileName
        }
        //先冲缓存里面拿取对应的值
        var mSp = mSPMap[mFileName]

        //如果缓存里面拿取的为空，那么根据key值，重新实例化，并缓存到map中
        if(mSp == null){
            mSp = instanceSp(App.getApplication(), mSpType,mFileName!!)
            mSPMap[mFileName] = mSp
        }

        //返回对应的sp对象
        return mSp
    }

    /**
     * 获取实例化的ISharePreferences
     */
    private fun instanceSp(context: Context, type: String, fileName: String): ISharedPreferences {
        return when (type) {
            SP_TYPE_MMKV -> {
                MMKVImpl(context, fileName)

            }

            SP_TYPE_SP -> {
                SPImpl(context, fileName)
            }

            else -> {
                SPImpl(context, fileName)
            }
        }
    }


}
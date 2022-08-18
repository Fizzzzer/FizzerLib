package com.fizzer.lib

import android.app.Application
import com.fizzer.base.lib.sp.SpUtils

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SpUtils.INSTANCE.init(this,SpUtils.SP_TYPE_MMKV)
    }
}
package com.fizzer.lib

import android.app.Application
import com.fizzer.base.lib.sp.SpFactory

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SpFactory.init(this,SpFactory.SP_TYPE_SP,"Fizzer")
    }
}
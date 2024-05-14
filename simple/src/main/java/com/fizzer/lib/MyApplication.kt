package com.fizzer.lib

import android.app.Application
import com.fizzer.base.lib.android.application.App
import com.fizzer.base.lib.sp.SpFactory

class MyApplication : App() {
    override fun onCreate() {
        super.onCreate()
        SpFactory.init(this,SpFactory.SP_TYPE_SP,"Fizzer")
    }
}
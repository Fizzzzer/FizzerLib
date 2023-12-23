package com.fizzer.base.lib.android.application

import android.app.Application

/**
* @Author: Fizzer
* @Email: fizzer503@sina.com
* @Date: 2023/12/23
* @Descriptor: 基类的Application
*/
open class App : Application() {

    companion object {
        private lateinit var mApplication: App
        fun getApplication(): App {
            return mApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        mApplication = this
    }
}
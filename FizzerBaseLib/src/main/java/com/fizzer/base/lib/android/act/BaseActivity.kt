package com.fizzer.base.lib.android.act

import android.content.Intent
import android.os.Bundle
import android.os.Process
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay

/**
 * @Author: Fizzer
 * @Email: fizzer503@sina.com
 * @Date: 2023/12/23
 * @Descriptor: 基类Activity
 */
abstract class BaseActivity : AppCompatActivity() {

    /**
     * 重启APP
     */
    fun restartApp() {
        val restartIntent = packageName?.let {
            packageManager?.getLaunchIntentForPackage(it)
        }
        restartIntent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        Process.killProcess(Process.myPid())
        startActivity(restartIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preInit()
        initView()
        initEvent()
        lifecycleScope.launchWhenResumed() {
            delay(500)
            lazyInitData()
        }
    }


    /**
     * 主要是在这里面做一些预初始化的东西，比如说intent取值的获取
     */
    open fun preInit(){ }
    abstract fun initView()
    abstract fun initEvent()
    abstract fun lazyInitData()
}
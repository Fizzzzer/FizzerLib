package com.fizzer.base.lib.android.act

import android.os.Bundle
import androidx.viewbinding.ViewBinding

/**
 * 使用了ViewBinding的Activity基类
 */
abstract class BaseVBActivity<VB : ViewBinding> : BaseActivity() {
    lateinit var binding: VB
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = bindingInflate()
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
    }

    abstract fun bindingInflate(): VB
}
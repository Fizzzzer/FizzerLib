package com.fizzer.base.lib.android.act

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.fizzer.base.lib.android.BaseViewModel

/**
 * 使用了ViewBinding 和ViewModel的Activity基类
 */
abstract class BaseVMActivity<VB : ViewBinding, VM : BaseViewModel> : BaseVBActivity<VB>() {

    var mViewModel: VM? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        getViewModel()?.let {
            mViewModel = ViewModelProvider(this)[it]
        }
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenResumed {
            initBaseViewModel()
        }
    }

    /**
     * 初始化基类的ViewModel
     */
    private fun initBaseViewModel() {
        //Toast
        mViewModel?.toastLiveData?.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    abstract fun getViewModel(): Class<VM>?
}
package com.fizzer.base.lib.android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @Author fizzer
 * @Data 2021/7/15 - 12:33 上午
 * @Email Fizzer53@sina.com
 * @Describe:
 */
class BaseViewModel : ViewModel() {
    val toastLiveData: LiveData<String> get() = _toastLiveData
    private val _toastLiveData = MutableLiveData("")

    /**
     * 显示Toast
     */
    fun showToast(msg: String?) {
        msg?.let {
            _toastLiveData.postValue(it)
        }
    }
}
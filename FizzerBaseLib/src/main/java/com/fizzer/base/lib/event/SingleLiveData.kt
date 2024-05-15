package com.fizzer.base.lib.event

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @Author Fizzer
 * @Email fizzer503@gmail.com
 * @Date 2024/5/15
 * @Desc 只允许被监听一次的LiveData，当数据被消费一次后，就不会再进行第二次消费了
 */
class SingleLiveData<T> : MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }

        if (pending.compareAndSet(true, false)) {
            super.observe(owner, observer)
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }


    companion object {
        private val TAG = "SingleLiveEvent"
    }
}
package com.fizzer.lib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fizzer.base.lib.ext.clickWithTrigger
import com.fizzer.base.lib.sp.SpUtils
import com.fizzer.fizzer_base.log.LogUtils
import com.fizzer.fizzer_base.sp.SPImpl
import com.fizzer.lib.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView(){
        binding.create.clickWithTrigger {
            SpUtils.INSTANCE.mSp?.putInt("Fizzer",78)
        }
    }
}
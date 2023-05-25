package com.fizzer.lib.lib

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fizzer.base.lib.ext.clickWithTrigger
import com.fizzer.base.lib.toast.ToastUtils
import com.fizzer.base.lib.utils.SystemSettingUtils
import com.fizzer.lib.databinding.ActivityLibPageBinding

class LibPage : AppCompatActivity() {

    private lateinit var binding: ActivityLibPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initEvent()
    }

    private fun initEvent(){

        binding.openAirMode.clickWithTrigger { SystemSettingUtils.setAirPlaneMode(this,true) }
        binding.closeAirMode.clickWithTrigger { SystemSettingUtils.setAirPlaneMode(this,false) }
        binding.getAirMode.clickWithTrigger { Toast.makeText(this,"AirPlaneMode = ${SystemSettingUtils.isAirModeOn(this)}",Toast.LENGTH_LONG).show() }
    }
}
package com.fizzer.lib.lib

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fizzer.base.lib.ext.clickWithTrigger
import com.fizzer.base.lib.log.LogUtils
import com.fizzer.base.lib.sp.SPDelegate
import com.fizzer.lib.databinding.ActivityLibPageBinding

class LibPage : AppCompatActivity() {

    private var password by SPDelegate("Fizzer","1")
    private var password1 by SPDelegate("p",6, fileName = "hello")
    private lateinit var binding: ActivityLibPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initEvent()
    }

    private fun initEvent(){

        binding.openAirMode.clickWithTrigger {
            Toast.makeText(this,password + password1,Toast.LENGTH_SHORT).show()
        }
        binding.closeAirMode.clickWithTrigger {
            password = "Fizzer"
        }
        binding.getAirMode.clickWithTrigger {
            password1 = 9
        }
    }
}
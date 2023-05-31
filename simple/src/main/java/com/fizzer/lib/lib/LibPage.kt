package com.fizzer.lib.lib

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fizzer.base.lib.ext.clickWithTrigger
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

        binding.openAirMode.clickWithTrigger {  }
        binding.closeAirMode.clickWithTrigger {  }
        binding.getAirMode.clickWithTrigger {  }
    }
}
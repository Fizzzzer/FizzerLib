package com.fizzer.lib

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.fizzer.base.lib.ext.clickWithTrigger
import com.fizzer.base.lib.log.LogUtils
import com.fizzer.lib.databinding.ActivityMainBinding
import com.fizzer.lib.lib.LibPage
import com.fizzer.lib.widget.WidgetPage
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        //这里是dev的提交
    }

    private fun initView() {
        binding.widget.clickWithTrigger {
            startActivity(Intent(this, WidgetPage::class.java))
        }

        binding.lib.clickWithTrigger {
            startActivity(Intent(this, LibPage::class.java))
        }
    }
}
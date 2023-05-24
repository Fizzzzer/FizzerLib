package com.fizzer.lib

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fizzer.base.lib.ext.clickWithTrigger
import com.fizzer.lib.databinding.ActivityMainBinding
import com.fizzer.lib.widget.WidgetPage

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.widget.clickWithTrigger {
            startActivity(Intent(this, WidgetPage::class.java))
        }
    }
}
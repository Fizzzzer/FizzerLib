package com.fizzer.lib.widget

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fizzer.base.lib.ext.clickWithTrigger
import com.fizzer.lib.databinding.ActivityWidgetPageBinding

class WidgetPage : AppCompatActivity() {

    private lateinit var binding: ActivityWidgetPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWidgetPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initEvent()
    }

    private fun initEvent() {
        binding.circleProgressBar.clickWithTrigger {
            startActivity(Intent(this, CircleProgressBarPage::class.java))
        }

        binding.roundCardView.clickWithTrigger {
            startActivity(Intent(this, RadiusCardViewPage::class.java))
        }
        binding.LetterIndexBar.clickWithTrigger {
            startActivity(Intent(this,LetterIndexBarPage::class.java))
        }
    }
}
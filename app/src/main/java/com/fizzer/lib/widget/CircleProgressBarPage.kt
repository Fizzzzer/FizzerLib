package com.fizzer.lib.widget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fizzer.base.lib.ext.clickWithTrigger
import com.fizzer.lib.databinding.ActivityCircleProgressBarPageBinding

class CircleProgressBarPage : AppCompatActivity() {

    private lateinit var binding: ActivityCircleProgressBarPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCircleProgressBarPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.setting.clickWithTrigger {

            var progress = binding.progress.text.toString()
            if (progress.isNullOrEmpty()) {
                return@clickWithTrigger
            }
            binding.cpb.setText("$progress %")
            binding.cpb.setProgress(progress = progress.toFloat())
        }
    }
}
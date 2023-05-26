package com.fizzer.lib.widget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fizzer.lib.databinding.ActivityRadiusCardPageBinding

class RadiusCardViewPage : AppCompatActivity() {
    private lateinit var binding: ActivityRadiusCardPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRadiusCardPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
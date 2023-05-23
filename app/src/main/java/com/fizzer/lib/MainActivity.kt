package com.fizzer.lib

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fizzer.lib.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
//        binding.create.clickWithTrigger {
//            SpUtils.INSTANCE.mSp?.file("Fizzer")?.putInt("Fizzer", 800)
//
//        }

//        binding.show.clickWithTrigger {
//            binding.value.text =
//                (SpUtils.INSTANCE.mSp?.file("Fizzer")?.getInt("Fizzer", 90) ?: "null").toString()
//        }
    }
}
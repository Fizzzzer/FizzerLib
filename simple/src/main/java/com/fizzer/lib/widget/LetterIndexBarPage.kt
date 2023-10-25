package com.fizzer.lib.widget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fizzer.lib.databinding.ActivityLetterIndexBarBinding

class LetterIndexBarPage : AppCompatActivity() {

    private lateinit var mBinding: ActivityLetterIndexBarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLetterIndexBarBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }
}
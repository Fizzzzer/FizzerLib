package com.fizzer.lib.lib

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fizzer.base.lib.android.act.BaseActivity
import com.fizzer.base.lib.ext.clickWithTrigger
import com.fizzer.base.lib.log.LogUtils
import com.fizzer.base.lib.sp.SPDelegate
import com.fizzer.base.lib.utils.AppInfoUtils
import com.fizzer.base.lib.utils.AppJumpUtils
import com.fizzer.lib.databinding.ActivityLibPageBinding

class LibPage : BaseActivity() {

    private var password by SPDelegate("Fizzer","1")
    private var password1 by SPDelegate("p",6, fileName = "hello")
    private lateinit var binding: ActivityLibPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLibPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
    }

    override fun initEvent(){

        binding.openAirMode.clickWithTrigger {
            Toast.makeText(this,password + password1,Toast.LENGTH_SHORT).show()
        }
        binding.closeAirMode.clickWithTrigger {
            password = "Fizzer"
        }
        binding.getAirMode.clickWithTrigger {
            restartApp()
        }

        binding.openMarket.clickWithTrigger {
            AppJumpUtils.gotoMarket(context = this)
        }
    }

    override fun lazyInitData() {
    }
}
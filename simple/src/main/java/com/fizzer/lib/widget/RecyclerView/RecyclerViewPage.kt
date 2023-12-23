package com.fizzer.lib.widget.RecyclerView

import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fizzer.base.lib.android.act.BaseActivity
import com.fizzer.base.lib.utils.UIUtils
import com.fizzer.lib.databinding.ActivityRecyclerViewPageBinding
import com.fizzer.widget.recycler.OffsetItemDecoration
import com.fizzer.widget.recycler.DividerItemDecoration

class RecyclerViewPage : BaseActivity() {

    private lateinit var binding: ActivityRecyclerViewPageBinding
    private val offsetItemDecoration by lazy {
        OffsetItemDecoration(
            startOffset = UIUtils.dp2px(context = baseContext, 50f),
            startOffsetColor = Color.parseColor("#FF0000"),
            endOffset = UIUtils.dp2px(context = baseContext, 200f),
            endOffsetColor = Color.parseColor("#00FF00")
        )
    }

    private val dividerItemDecoration by lazy {
        DividerItemDecoration().apply {
            showEnd = true
            showStart = true
            dividerMarginStart = UIUtils.dp2px(baseContext,10f)
            dividerMarginEnd = UIUtils.dp2px(baseContext,10f)
            dividerGap = 10
            dividerColor = Color.parseColor("#FF0000")
        }
    }

    private val linearLayoutManager by lazy {
        LinearLayoutManager(
            baseContext,
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    private val gridLayoutManager by lazy { GridLayoutManager(baseContext, 3) }

    private val mAdapter by lazy { BaseItemAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerViewPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun getTestData(): MutableList<String> {
        val list = mutableListOf<String>()
        for (index in 0 until 10) {
            list.add(index.toString())
        }
        return list
    }

    private fun initView() {
        binding.rv.apply {
            layoutManager = linearLayoutManager
            adapter = mAdapter
            addItemDecoration(dividerItemDecoration)
            mAdapter.data = getTestData()
        }
    }
}
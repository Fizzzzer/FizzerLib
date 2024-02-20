package com.fizzer.lib.widget.RecyclerView

import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fizzer.base.lib.android.act.BaseActivity
import com.fizzer.base.lib.android.act.BaseVBActivity
import com.fizzer.base.lib.utils.UIUtils
import com.fizzer.lib.databinding.ActivityRecyclerViewPageBinding
import com.fizzer.widget.recycler.OffsetItemDecoration
import com.fizzer.widget.recycler.DividerItemDecoration
import com.fizzer.widget.recycler.TagItemDecoration

class RecyclerViewPage : BaseVBActivity<ActivityRecyclerViewPageBinding>() {

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
            showEnd = false
            showStart = false
            dividerMarginStart = UIUtils.dp2px(baseContext, 10f)
            dividerMarginEnd = UIUtils.dp2px(baseContext, 10f)
            dividerGap = 10
            dividerColor = Color.parseColor("#00FF00")
        }
    }

    private val tagItemDecoration by lazy {
        TagItemDecoration.ItemBuilder().apply {
            setDataSource(getTestData())
            setTagTextColor(Color.parseColor("#0000FF"))
            setTagTextSize(UIUtils.sp2px(baseContext,18f))
            setItemTagHeight(UIUtils.dp2px(baseContext,25f))
            setItemMargin(UIUtils.dp2px(baseContext,15f))
            setItemBgColor(Color.parseColor("#FF0000"))
        }.builder()
    }

    private val linearLayoutManager by lazy {
        LinearLayoutManager(
            baseContext,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private val gridLayoutManager by lazy { GridLayoutManager(baseContext, 3) }

    private val mAdapter by lazy { BaseItemAdapter() }

    override fun bindingInflate() = ActivityRecyclerViewPageBinding.inflate(layoutInflater)

    private fun getTestData(): MutableList<ItemData> {
        val list = mutableListOf<ItemData>()
        for (index in 0 until 30) {
            when{
                index < 5 -> list.add(ItemData("A$index"))
                index < 10 -> list.add(ItemData("B$index"))
                index < 15 -> list.add(ItemData("C$index"))
                index < 20 -> list.add(ItemData("D$index"))
                index < 25 -> list.add(ItemData("E$index"))
                index < 30 -> list.add(ItemData("F$index"))
            }
        }
        return list
    }

    override fun initView() {
        binding.rv.apply {
            layoutManager = linearLayoutManager
            adapter = mAdapter

            addItemDecoration(tagItemDecoration)
            mAdapter.data = getTestData()
        }
    }

    override fun initEvent() {
    }

    override fun lazyInitData() {
    }
}
package com.fizzer.base.lib.ext

import android.util.Log
import android.view.View.MeasureSpec
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewExt {

    /**
     * 获取LinearLayoutManager的纵向高度
     * 只针对方向为竖直方向上的
     */
    fun RecyclerView.getRecyclerHeight():Int{
        val lm = this.layoutManager ?: return 0
        var totalH = 0
        for (index in 0 until lm.itemCount) {
            val item = lm.findViewByPosition(index)
            if (item == null) {
                val holder = adapter!!.createViewHolder(
                    this,adapter!!.getItemViewType(index)
                )
                adapter!!.bindViewHolder(holder, index)
                val itemView = holder.itemView
                val widthSpec = MeasureSpec.makeMeasureSpec(
                    width, MeasureSpec.EXACTLY
                )
                val heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                itemView.measure(widthSpec, heightSpec)
                val height = itemView.measuredHeight
                totalH += height
            } else {
                val height = item.measuredHeight
                totalH += height
            }
        }
        return totalH
    }
}
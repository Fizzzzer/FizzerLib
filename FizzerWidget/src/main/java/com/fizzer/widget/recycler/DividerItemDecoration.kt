package com.fizzer.widget.recycler

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class DividerItemDecoration(
    var dividerGap: Int = 0, var dividerColor: Int = -1,
    var dividerMarginStart: Int = 0, var dividerMarginEnd: Int = 0,
    var showStart: Boolean = false, var showEnd: Boolean = false
) : ItemDecoration() {

    private val mPaint by lazy {
        Paint().apply {
            isAntiAlias = true
        }
    }

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {

        if (parent.layoutManager is GridLayoutManager) return
        if (parent.layoutManager is LinearLayoutManager) {
            when ((parent.layoutManager as LinearLayoutManager).orientation) {
                RecyclerView.VERTICAL -> initVerticalStyle(outRect, view, parent, state)
                RecyclerView.HORIZONTAL -> initHorizontalStyle(outRect, view, parent, state)
            }
        }
    }

    //设置竖直方向上的分割线样式
    private fun initVerticalStyle(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {


        if (showStart) {
            outRect.top = dividerGap
        } else {
            //如果不显示首条的分割线，那么首条分割线不设置top属性
            if (parent.getChildLayoutPosition(view) != 0) {
                outRect.top = dividerGap
            }
        }

        //如果需要显示最后一条的分割线，那么判断视图的位置，然后给最后一条view设置bottom属性
        if (showEnd) {
            if (parent.getChildLayoutPosition(view) == (parent.layoutManager?.itemCount ?: 0) - 1) {
                outRect.bottom = dividerGap
            }
        }
    }

    //设置水平方向上的分割线样式
    private fun initHorizontalStyle(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {

        if (showStart) {
            outRect.left = dividerGap
        } else {
            if (parent.getChildLayoutPosition(view) != 0) {
                outRect.left = dividerGap
            }
        }

        if (showEnd) {
            if (parent.getChildLayoutPosition(view) == (parent.layoutManager?.itemCount ?: 0) - 1) {
                outRect.right = dividerGap
            }
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        if (parent.layoutManager is GridLayoutManager) return
        if (parent.layoutManager is LinearLayoutManager) {
            when ((parent.layoutManager as LinearLayoutManager).orientation) {
                RecyclerView.HORIZONTAL -> drawHorizontalDivider(c, parent, state)
                RecyclerView.VERTICAL -> drawVerticalDivider(c, parent, state)
            }
        }
    }

    //绘制水平分割线
    private fun drawHorizontalDivider(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        val startIndex = if (showStart) 0 else 1
        for (index in startIndex until parent.childCount) {
            val curView = parent.getChildAt(index)
            val startX = curView.left - dividerGap
            val startY = curView.top + dividerMarginStart
            val endX = curView.left
            val endY = curView.bottom - dividerMarginEnd
            mPaint.color = dividerColor
            c.drawRect(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat(), mPaint)
        }

        //是否绘制最后一个
        if (showEnd) {
            val lastView = parent.getChildAt(parent.childCount - 1)
            var startX = lastView.right
            val startY = lastView.top + dividerMarginStart
            val endX = lastView.right + dividerGap
            val endY = lastView.bottom - dividerMarginEnd
            mPaint.color = dividerColor
            c.drawRect(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat(), mPaint)
        }
    }

    //绘制竖直方向的分割线
    private fun drawVerticalDivider(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val startIndex = if (showStart) 0 else 1
        for (index in startIndex until parent.childCount) {
            val curView = parent.getChildAt(index)
            val startX = curView.left + dividerMarginStart
            val startY = curView.top - dividerGap
            val endX = curView.right - dividerMarginEnd
            val endY = curView.top
            mPaint.color = dividerColor
            c.drawRect(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat(), mPaint)
        }

        //是否绘制最后一个
        if (showEnd) {
            val lastView = parent.getChildAt(parent.childCount - 1)
            val startX = lastView.left + dividerMarginStart
            val startY = lastView.bottom
            val endX = lastView.right - dividerMarginEnd
            val endY = lastView.bottom + dividerGap
            mPaint.color = dividerColor
            c.drawRect(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat(), mPaint)
        }
    }
}
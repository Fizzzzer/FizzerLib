package com.fizzer.widget.recycler

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * @Author: Fizzer
 * @Email: fizzer503@sina.com
 * @Date: 2023/12/23
 * @Descriptor: 首尾留出空白的Item修饰器,仅对LinearLayoutManager
 *
 *
 * @param startOffset RecyclerView开头留出的空白区域大小
 * @param startOffsetColor 开头留出空白的背景颜色
 * @param endOffset  尾部留出空白的区域大小
 * @param endOffsetColor  尾部留出空白的背景颜色
 *
 */
class OffsetItemDecoration(
    var startOffset: Int = 0, var startOffsetColor: Int = -1,
    var endOffset: Int = 0, var endOffsetColor: Int = -1
) : ItemDecoration() {

    private val mPaint by lazy {
        Paint().apply {
            isAntiAlias = true
        }
    }

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.layoutManager is GridLayoutManager) return
        if (parent.layoutManager is LinearLayoutManager) {
            when ((parent.layoutManager as LinearLayoutManager).orientation) {
                RecyclerView.HORIZONTAL -> initHorizontalStyle(outRect, view, parent, state)
                RecyclerView.VERTICAL -> initVerticalStyle(outRect, view, parent, state)
            }
        }
    }

    /**
     * 设置水平方向上首尾留白区域
     */
    private fun initHorizontalStyle(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.left = startOffset
        } else if (
            parent.getChildLayoutPosition(view) == (parent.layoutManager?.itemCount ?: 0) - 1) {
            outRect.right = endOffset
        }
    }

    /**
     * 设置竖直方向上首尾留白区域
     */
    private fun initVerticalStyle(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = startOffset
        } else if (
            parent.getChildLayoutPosition(view) == (parent.layoutManager?.itemCount ?: 0) - 1) {
            outRect.bottom = endOffset
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if(parent.layoutManager is GridLayoutManager) return
        if (parent.layoutManager is LinearLayoutManager) {
            when ((parent.layoutManager as LinearLayoutManager).orientation) {
                RecyclerView.HORIZONTAL -> drawHorizontalOffset(c, parent, state)
                RecyclerView.VERTICAL -> drawVerticalOffset(c, parent, state)
            }
        }
    }

    /**
     * 绘制水平方向上的首尾空白区域颜色
     */
    private fun drawHorizontalOffset(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (startOffsetColor != -1) {
            val startView = parent.getChildAt(0)
            mPaint.color = startOffsetColor
            val mRect = RectF(0f, 0f, startOffset.toFloat(), startView.bottom.toFloat())
            c.drawRect(mRect, mPaint)
        }

        if (endOffsetColor != -1) {
            val endView = parent.getChildAt(parent.childCount - 1)
            mPaint.color = endOffsetColor
            val mRect = RectF(
                endView.right.toFloat(),
                0f,
                (endView.right + endOffset).toFloat(),
                endView.bottom.toFloat()
            )
            c.drawRect(mRect, mPaint)
        }
    }

    /**
     * 绘制竖直方向上的首尾空白背景颜色
     */
    private fun drawVerticalOffset(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        if (startOffsetColor != -1) {
            val startView = parent.getChildAt(0)
            mPaint.color = startOffsetColor
            val mRect = RectF(0f, 0f, startView.right.toFloat(), startView.top.toFloat())
            c.drawRect(mRect, mPaint)
        }

        if (endOffsetColor != -1) {
            val endView = parent.getChildAt(parent.childCount - 1)
            mPaint.color = endOffsetColor
            val mRect = RectF(
                0f,
                endView.bottom.toFloat(),
                endView.right.toFloat(),
                (endView.bottom + endOffset).toFloat()
            )
            c.drawRect(mRect, mPaint)
        }
    }

}
package com.fizzer.widget.recycler

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import java.lang.RuntimeException

/**
 * @Author: Fizzer
 * @Email: fizzer503@sina.com
 * @Date: 2024/2/20
 * @Descriptor: 标签分组的item装饰器
 */
class TagItemDecoration private constructor(private var builder: ItemBuilder) : ItemDecoration() {

    //绘制背景的画笔
    private val mBgPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = builder.itemTagBgColor
        }
    }

    //绘制文本的画笔
    private val mTagTextPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = builder.tagTextColor
            textSize = builder.tagTextSize.toFloat()
        }
    }

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        if (builder.mData.isEmpty()) {
            throw RuntimeException("请先设置数据源")
        }

        //如果当前的layoutManager是LinearLayoutManager并且方向为垂直方向，则进行绘制
        if (parent.layoutManager is LinearLayoutManager && (parent.layoutManager as LinearLayoutManager).orientation == LinearLayoutManager.VERTICAL) {
            val position = parent.getChildLayoutPosition(view)
            if (position < 0) return

            if (position == 0) {
                //如果位置为0，那么是肯定需要显示tag视图的
                outRect.set(0, builder.itemTagHeight, 0, 0)
            } else {
                //如果数据源不相同，就显示tag视视图，如果相同，那么就不显示tag视图
                if (builder.mData[position].getTitleTag() != builder.mData[position - 1].getTitleTag()) {
                    outRect.set(0, builder.itemTagHeight, 0, 0)
                } else {
                    outRect.set(0, 0, 0, 0)
                }
            }
        } else {
            throw RuntimeException("不支持当前的LayoutManager")
        }
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (parent.layoutManager is LinearLayoutManager && (parent.layoutManager as LinearLayoutManager).orientation == LinearLayoutManager.VERTICAL) {
            //绘制Tag
            drawTagArea(c, parent, state)
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (!builder.fixable) {
            return
        }

        val position = (parent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        val currentView = parent.findViewHolderForLayoutPosition(position)?.itemView
        val tag = builder.mData[position].getTitleTag()

        currentView?.let {
            var flag = false
            //防止数组越界
            if ((position + 1) < builder.mData.size) {
                //当前第一个可见的Item的tag，不等于其后一个item的tag，说明悬浮的View要切换了
                if (tag != builder.mData[position + 1].getTitleTag()) {
                    //当第一个可见的item在屏幕中还剩的高度小于title区域的高度时，我们也该开始做悬浮Title的“交换动画”
                    if ((it.height + it.top) < builder.itemTagHeight) {
                        c.save()
                        flag = true
                        c.translate(
                            0f,
                            (currentView.height + currentView.top - builder.itemTagHeight).toFloat()
                        )
                    }
                }
            }

            val startX = currentView.left.toFloat()
            val startY = 0f
            val endX = currentView.right.toFloat()
            val endY = builder.itemTagHeight.toFloat()
            c.drawRect(startX, startY, endX, endY, mBgPaint)

            val mBounds = Rect()
            mTagTextPaint.getTextBounds(tag, 0, tag.length, mBounds)
            val tagX = (currentView.left + builder.itemTagMargin).toFloat()
            val tagY =
                (builder.itemTagHeight - (builder.itemTagHeight / 2 - mBounds.height() / 2)).toFloat()
            c.drawText(tag, tagX, tagY, mTagTextPaint)
            if (flag) c.restore()
        }
    }


    /**
     * 绘制Tag视图
     */
    private fun drawTagArea(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        if (builder.mData.isEmpty()) {
            throw RuntimeException("请先设置数据源")
        }
        for (index in 0 until parent.childCount) {
            val childView = parent.getChildAt(index)
            val params = childView.layoutParams as RecyclerView.LayoutParams
            val position = params.viewLayoutPosition

            if (position == 0) {
                drawTagBg(c, parent, position, childView)
                drawTagText(c, parent, position, childView)
            } else {
                if (builder.mData[position].getTitleTag() != builder.mData[position - 1].getTitleTag()) {
                    drawTagBg(c, parent, position, childView)
                    drawTagText(c, parent, position, childView)
                }
            }
        }
    }

    /**
     * 绘制Tag的背景视图
     */
    private fun drawTagBg(c: Canvas, parent: RecyclerView, position: Int, childView: View) {

        val startX = childView.left
        val startY = childView.top - builder.itemTagHeight

        val endX = childView.right
        val endY = childView.top
        c.drawRect(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat(), mBgPaint)
    }

    /**
     * 绘制Tag的文字
     */
    private fun drawTagText(c: Canvas, parent: RecyclerView, position: Int, childView: View) {
        val tag = builder.mData[position].getTitleTag()
        val mBounds = Rect()
        mTagTextPaint.getTextBounds(tag, 0, tag.length, mBounds)
        val startX = (childView.left + builder.itemTagMargin).toFloat()
        val startY =
            (childView.top - (builder.itemTagHeight / 2 - mBounds.height() / 2)).toFloat()
        c.drawText(tag, startX, startY, mTagTextPaint)
    }


    class ItemBuilder {
        //Tag的数据源
        internal var mData = mutableListOf<IItemTitle>()

        //Tag视图的高度
        internal var itemTagHeight: Int = 40

        //tag的边距
        internal var itemTagMargin: Int = 0

        //Tag视图布局的背景颜色
        internal var itemTagBgColor: Int = Color.parseColor("#000000")

        //Tag的文本颜色值
        internal var tagTextColor: Int = Color.parseColor("#FFFFFF")

        //Tag的文本字体大小
        internal var tagTextSize: Int = 18

        //标签是否可固定在界面的顶部，默认为true，
        internal var fixable: Boolean = true

        /**
         * 设置Item Tag的高度
         */
        fun setItemTagHeight(tagHeight: Int) = apply { itemTagHeight = tagHeight }

        /**
         * 设置数据源
         */
        fun setDataSource(list: List<IItemTitle>) = apply {
            mData.clear()
            list.forEach {
                mData.add(it)
            }
        }

        /**
         * 设置Item的margin值
         */
        fun setItemMargin(margin: Int) = apply { itemTagMargin = margin }

        /**
         * 设置Item的背景颜色
         */
        fun setItemBgColor(color: Int) = apply { itemTagBgColor = color }

        /**
         * 设置tag的文本颜色值
         */
        fun setTagTextColor(color: Int) = apply { tagTextColor = color }

        /**
         * 设置Tag是否固定在界面的顶部
         * 如果设置为false，那么标签会随着滑动滑出界面外
         */
        fun setTagFixable(fix: Boolean) = apply { fixable = fix }

        /**
         * 设置tag的文本字体大小
         */
        fun setTagTextSize(size: Int) = apply { tagTextSize = size }
        fun builder(): TagItemDecoration {
            return TagItemDecoration(this)
        }
    }

    interface IItemTitle {
        fun getTitleTag(): String
    }
}
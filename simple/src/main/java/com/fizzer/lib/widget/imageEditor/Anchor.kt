package com.fizzer.lib.widget.imageEditor

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log

class Anchor private constructor() {
    //左上
    private val LEFT_TOP = PointF()
    private val LEFT_TOP_RECTF = RectF()

    //右上
    private val RIGHT_TOP = PointF()
    private val RIGHT_TOP_RECTF = RectF()

    //左下
    private val LEFT_BOTTOM = PointF()
    private val LEFT_BOTTOM_RECTF = RectF()

    //右下
    private val RIGHT_BOTTOM = PointF()
    private val RIGHT_BOTTOM_RECTF = RectF()

    private val gap = 2

    private val mCornerWidth = ImageEditConfig.CORNER_WIDTH

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { Anchor() }
    }

    fun resetAnchor(rectF: RectF) {
        LEFT_TOP.set(rectF.left - mCornerWidth / 2 - gap, rectF.top - mCornerWidth / 2 - gap)

        RIGHT_TOP.set(rectF.right + mCornerWidth / 2 + gap, rectF.top - mCornerWidth / 2 - gap)

        LEFT_BOTTOM.set(rectF.left - mCornerWidth / 2 - gap, rectF.bottom + mCornerWidth / 2 + gap)

        RIGHT_BOTTOM.set(
            rectF.right + mCornerWidth / 2 + gap,
            rectF.bottom + mCornerWidth / 2 + gap
        )

        resetAnchorRect()
    }

    //更新锚点的点击区域返回，用来判断用户滑动的时候，是哪个锚点在动
    private fun resetAnchorRect() {
        LEFT_TOP_RECTF.set(
            LEFT_TOP.x, LEFT_TOP.y,
            LEFT_TOP.x + ImageEditConfig.CORNER_LENGTH,
            LEFT_TOP.y + ImageEditConfig.CORNER_LENGTH
        )

        RIGHT_TOP_RECTF.set(
            RIGHT_TOP.x - ImageEditConfig.CORNER_LENGTH, RIGHT_TOP.y,
            RIGHT_TOP.x,
            RIGHT_TOP.y + ImageEditConfig.CORNER_LENGTH
        )

        LEFT_BOTTOM_RECTF.set(
            LEFT_BOTTOM.x, LEFT_BOTTOM.y - ImageEditConfig.CORNER_LENGTH,
            LEFT_BOTTOM.x + ImageEditConfig.CORNER_LENGTH,
            LEFT_BOTTOM.y
        )

        RIGHT_BOTTOM_RECTF.set(
            RIGHT_BOTTOM.x - ImageEditConfig.CORNER_LENGTH,
            RIGHT_BOTTOM.y - ImageEditConfig.CORNER_LENGTH,
            RIGHT_BOTTOM.x,
            RIGHT_BOTTOM.y,
        )
    }

    fun getLeftTopAnchor() = LEFT_TOP

    fun getRightTopAnchor() = RIGHT_TOP

    fun getLeftBottomAnchor() = LEFT_BOTTOM

    fun getRightBottomAnchor() = RIGHT_BOTTOM

    fun getAnchorByLocation(x: Float, y: Float): PointF? {
        return when {
            RIGHT_TOP_RECTF.contains(x, y) -> {
                Log.e("ImageEditor", "右上")
                RIGHT_TOP
            }

            LEFT_TOP_RECTF.contains(x, y) -> {
                Log.e("ImageEditor", "左上")
                LEFT_TOP
            }


            LEFT_BOTTOM_RECTF.contains(x, y) -> {
                Log.e("ImageEditor", "左下")
                LEFT_BOTTOM
            }

            RIGHT_BOTTOM_RECTF.contains(x, y) -> {
                Log.e("ImageEditor", "右下")
                RIGHT_BOTTOM
            }

            else -> {
                Log.e("ImageEditor", "空")
                null
            }
        }
    }

    private val paint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.RED
    }

//    fun drawRect(canvas: Canvas?) {
//        canvas?.drawRect(LEFT_TOP_RECTF, paint)
//        canvas?.drawRect(LEFT_BOTTOM_RECTF, paint)
//        canvas?.drawRect(RIGHT_TOP_RECTF, paint)
//        canvas?.drawRect(RIGHT_BOTTOM_RECTF, paint)
//    }
}
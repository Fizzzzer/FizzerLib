package com.fizzer.lib.widget.imageEditor

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.view.ContentInfoCompat.Flags

class ClipFrameView private constructor() {
    private var clipRectMargin = 40f

    private var mClipRect: RectF = RectF()

    private val mTopLeftAnchor = Anchor.TopLeftAnchor()
    private val mTopRightAnchor = Anchor.TopRightAnchor()
    private val mBottomLeftAnchor = Anchor.BottomLeftAnchor()
    private val mBottomRightAnchor = Anchor.BottomRightAnchor()

    private val mClipPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f
        color = Color.WHITE
        strokeCap = Paint.Cap.SQUARE
    }

    private val mClipCornerPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = ImageEditConfig.CORNER_WIDTH
        color = Color.WHITE
        strokeCap = Paint.Cap.SQUARE
    }

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { ClipFrameView() }
    }

    fun initClipFrameSize(width: Float, height: Float) {
        mClipRect.set(
            clipRectMargin, clipRectMargin,
            width - clipRectMargin, height - clipRectMargin
        )
    }

    fun getClipRect() = mClipRect


    fun onDraw(canvas: Canvas?) {
        canvas?.drawRect(mClipRect, mClipPaint)
        //绘制的时候，重置锚点位置
        resetAnchor(mClipRect)

        val topLeftAnchor = mTopLeftAnchor.anchorPoint
        //左上角
        canvas?.drawLine(
            topLeftAnchor.x, topLeftAnchor.y,
            topLeftAnchor.x + ImageEditConfig.CORNER_LENGTH, topLeftAnchor.y,
            mClipCornerPaint
        )
        canvas?.drawLine(
            topLeftAnchor.x, topLeftAnchor.y,
            topLeftAnchor.x, topLeftAnchor.y + ImageEditConfig.CORNER_LENGTH,
            mClipCornerPaint
        )

        //右上角
        val topRightAnchor = mTopRightAnchor.anchorPoint
        canvas?.drawLine(
            topRightAnchor.x, topRightAnchor.y,
            topRightAnchor.x - ImageEditConfig.CORNER_LENGTH, topRightAnchor.y,
            mClipCornerPaint
        )
        canvas?.drawLine(
            topRightAnchor.x, topRightAnchor.y,
            topRightAnchor.x, topRightAnchor.y + ImageEditConfig.CORNER_LENGTH,
            mClipCornerPaint
        )

        //左下角
        val bottomLeftAnchor = mBottomLeftAnchor.anchorPoint
        canvas?.drawLine(
            bottomLeftAnchor.x, bottomLeftAnchor.y,
            bottomLeftAnchor.x + ImageEditConfig.CORNER_LENGTH, bottomLeftAnchor.y,
            mClipCornerPaint
        )
        canvas?.drawLine(
            bottomLeftAnchor.x, bottomLeftAnchor.y,
            bottomLeftAnchor.x, bottomLeftAnchor.y - ImageEditConfig.CORNER_LENGTH,
            mClipCornerPaint
        )

        //右下角
        val bottomRightAnchor = mBottomRightAnchor.anchorPoint
        canvas?.drawLine(
            bottomRightAnchor.x, bottomRightAnchor.y,
            bottomRightAnchor.x - ImageEditConfig.CORNER_LENGTH, bottomRightAnchor.y,
            mClipCornerPaint
        )

        canvas?.drawLine(
            bottomRightAnchor.x, bottomRightAnchor.y,
            bottomRightAnchor.x, bottomRightAnchor.y - ImageEditConfig.CORNER_LENGTH,
            mClipCornerPaint
        )
    }

    private fun resetAnchor(clipFrame: RectF) {
        mTopLeftAnchor.initAnchor(clipFrame)
        mTopRightAnchor.initAnchor(clipFrame)
        mBottomLeftAnchor.initAnchor(clipFrame)
        mBottomRightAnchor.initAnchor(clipFrame)
    }

    fun getAnchorByLocation(x: Float, y: Float): Anchor? {
        return when {
            mTopLeftAnchor.anchorRect.contains(x, y) -> mTopLeftAnchor
            mTopRightAnchor.anchorRect.contains(x, y) -> mTopRightAnchor
            mBottomLeftAnchor.anchorRect.contains(x, y) -> mBottomLeftAnchor
            mBottomRightAnchor.anchorRect.contains(x, y) -> mBottomRightAnchor
            else -> null
        }
    }

    fun anchorMove(anchor: Anchor, dx: Float, dy: Float) {
        when (anchor) {
            is Anchor.TopLeftAnchor -> {
                mClipRect.set(
                    mClipRect.left + dx,
                    mClipRect.top + dy,
                    mClipRect.right,
                    mClipRect.bottom
                )
            }

            is Anchor.TopRightAnchor -> {
                mClipRect.set(
                    mClipRect.left,
                    mClipRect.top + dy,
                    mClipRect.right + dx,
                    mClipRect.bottom
                )
            }

            is Anchor.BottomLeftAnchor -> {
                mClipRect.set(
                    mClipRect.left + dx, mClipRect.top, mClipRect.right, mClipRect.bottom + dy
                )
            }

            is Anchor.BottomRightAnchor -> {
                mClipRect.set(
                    mClipRect.left,
                    mClipRect.top,
                    mClipRect.right + dx,
                    mClipRect.bottom + dy
                )
            }
        }
    }
}
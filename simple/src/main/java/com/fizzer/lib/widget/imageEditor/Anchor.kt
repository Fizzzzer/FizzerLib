package com.fizzer.lib.widget.imageEditor

import android.graphics.PointF
import android.graphics.RectF

sealed class Anchor {
    val anchorPoint: PointF = PointF()
    val anchorRect: RectF = RectF()
    var mType:String = ""

    abstract fun initAnchor(clipFrame: RectF)

    //左上
    data class TopLeftAnchor(var type: String = "Top_Left") : Anchor() {

        override fun initAnchor(clipFrame: RectF) {
            mType = type
            anchorPoint.set(
                clipFrame.left - ImageEditConfig.CORNER_WIDTH / 2 - ImageEditConfig.CORNER_GAP,
                clipFrame.top - ImageEditConfig.CORNER_WIDTH / 2 - ImageEditConfig.CORNER_GAP
            )

            anchorRect.set(
                anchorPoint.x, anchorPoint.y,
                anchorPoint.x + ImageEditConfig.CORNER_LENGTH,
                anchorPoint.y + ImageEditConfig.CORNER_LENGTH
            )
        }
    }

    //右上
    data class TopRightAnchor(val type: String = "Top_Right") : Anchor() {
        override fun initAnchor(clipFrame: RectF) {
            mType = type
            anchorPoint.set(
                clipFrame.right + ImageEditConfig.CORNER_WIDTH / 2 + ImageEditConfig.CORNER_GAP,
                clipFrame.top - ImageEditConfig.CORNER_WIDTH / 2 - ImageEditConfig.CORNER_GAP
            )

            anchorRect.set(
                anchorPoint.x - ImageEditConfig.CORNER_LENGTH, anchorPoint.y,
                anchorPoint.x,
                anchorPoint.y + ImageEditConfig.CORNER_LENGTH
            )
        }
    }

    //左下
    data class BottomLeftAnchor(val type: String = "Bottom_Left") : Anchor() {

        override fun initAnchor(clipFrame: RectF) {
            mType = type
            anchorPoint.set(
                clipFrame.left - ImageEditConfig.CORNER_WIDTH / 2 - ImageEditConfig.CORNER_GAP,
                clipFrame.bottom + ImageEditConfig.CORNER_WIDTH / 2 + ImageEditConfig.CORNER_GAP
            )

            anchorRect.set(
                anchorPoint.x, anchorPoint.y - ImageEditConfig.CORNER_LENGTH,
                anchorPoint.x + ImageEditConfig.CORNER_LENGTH,
                anchorPoint.y
            )
        }
    }

    //右下
    data class BottomRightAnchor(val type: String = "Bottom_Right") : Anchor() {

        override fun initAnchor(clipFrame: RectF) {
            mType = type
            anchorPoint.set(
                clipFrame.right + ImageEditConfig.CORNER_WIDTH / 2 + ImageEditConfig.CORNER_GAP,
                clipFrame.bottom + ImageEditConfig.CORNER_WIDTH / 2 + ImageEditConfig.CORNER_GAP
            )

            anchorRect.set(
                anchorPoint.x - ImageEditConfig.CORNER_LENGTH,
                anchorPoint.y - ImageEditConfig.CORNER_LENGTH,
                anchorPoint.x,
                anchorPoint.y,
            )
        }
    }


}
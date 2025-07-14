package com.fizzer.lib.widget.ImageEditor.editor.clip

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.RectF
import com.fizzer.lib.widget.ImageEditor.editor.PictureEditor
import com.fizzer.lib.widget.ImageEditor.editor.clip.EditClipWindow.IClipRender
import kotlin.math.cos
import kotlin.math.sin

class RotateClipRender : IClipRender {

    var CLIP_MARGIN: Float = PictureEditor.getInstance().getClipRectMarginNormal() // 裁剪区域的边距
    var CLIP_CORNER_SIZE: Float = 42f // 角尺寸
    var CLIP_THICKNESS_CELL: Float = 2f // 内边厚度
    var CLIP_THICKNESS_FRAME: Float = 4f //外边框厚度
    var CLIP_THICKNESS_SEWING: Float = 8f //角边厚度
    var COLOR_CELL: Int = Color.WHITE
    var COLOR_FRAME: Int = Color.WHITE
    var COLOR_CORNER: Int = Color.WHITE

    /**
     * 比例尺，用于计算出 {0, width, 1/3 width, 2/3 width} & {0, height, 1/3 height, 2/3 height}
     */
    private val CLIP_SIZE_RATIO = floatArrayOf(0f, 1f, 0.33f, 0.66f)
    private val CLIP_CORNER_STEPS = floatArrayOf(0f, 3f, -3f)
    private val CLIP_CORNER_SIZES = floatArrayOf(0f, CLIP_CORNER_SIZE, -CLIP_CORNER_SIZE)
    private val CLIP_CORNERS = byteArrayOf(
        0x8, 0x8, 0x9, 0x8,
        0x6, 0x8, 0x4, 0x8,
        0x4, 0x8, 0x4, 0x1,
        0x4, 0xA, 0x4, 0x8,
        0x4, 0x4, 0x6, 0x4,
        0x9, 0x4, 0x8, 0x4,
        0x8, 0x4, 0x8, 0x6,
        0x8, 0x9, 0x8, 0x8
    )
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var CLIP_CELL_STRIDES: Int = 0x7362DC98
    var CLIP_CORNER_STRIDES: Int = 0x0AAFF550
    private val mCells = FloatArray(16)
    private val mCorners = FloatArray(32)
    private val mBaseSizes = Array<FloatArray?>(2) { FloatArray(4) }



    //最大的旋转角度值
    private val MAX_ANGLE_VALUE = 80f

    //最小的旋转角度值
    private val MIN_ANGLE_VALUE = -80f

    //旋转的角度范围
    private val ANGLE_VALUE_RANGE = MAX_ANGLE_VALUE - MIN_ANGLE_VALUE

//    private var currentAngle = 0f // 当前轮盘角度

    //锚点
    private val mAnchorPoint = Point()

    //绘制弧形的半径
    private var mRadius: Double = 0.0

    private var mRotateFrame = RectF()

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 28f
        textAlign = Paint.Align.CENTER
    }


    init {
        mPaint.apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.SQUARE
        }

    }

    override fun getCornerSize() = CLIP_CORNER_SIZE

    override fun getClipMargin() = CLIP_MARGIN

    override fun onDraw(canvas: Canvas?, frame: RectF?,angle: Float) {
        val size = floatArrayOf(frame!!.width(), frame.height())
        for (i in mBaseSizes.indices) {
            for (j in mBaseSizes[i]!!.indices) {
                mBaseSizes[i]!![j] = size[i] * CLIP_SIZE_RATIO[j]
            }
        }

        for (i in mCells.indices) {
            mCells[i] = mBaseSizes[i and 1]!![CLIP_CELL_STRIDES ushr (i shl 1) and 3]
        }
        for (i in mCorners.indices) {
            mCorners[i] = (mBaseSizes[i and 1]!![CLIP_CORNER_STRIDES ushr i and 1]
                    + CLIP_CORNER_SIZES[CLIP_CORNERS[i].toInt() and 3] + CLIP_CORNER_STEPS[CLIP_CORNERS[i].toInt() shr 2])
        }

        //绘制内边框线
        canvas!!.translate(frame.left, frame.top)
        mPaint.style = Paint.Style.STROKE
        mPaint.setColor(COLOR_CELL)
        mPaint.strokeWidth = CLIP_THICKNESS_CELL
        canvas.drawLines(mCells, mPaint)

        //绘制外边框线
        canvas.translate(-frame.left, -frame.top)
        mPaint.setColor(COLOR_FRAME)
        mPaint.strokeWidth = CLIP_THICKNESS_FRAME
        canvas.drawRect(frame, mPaint)

        //绘制四个角
        canvas.translate(frame.left, frame.top)
        mPaint.setColor(COLOR_CORNER)
        mPaint.strokeWidth = CLIP_THICKNESS_SEWING
        canvas.drawLines(mCorners, mPaint)

        canvas.translate(-frame.left, -frame.top)
//        canvas.drawLine(
//            frame.left, frame.bottom + 10,
//            frame.right, frame.bottom + 10,
//            mPaint.apply { color = Color.YELLOW })
        drawRotateMark(canvas,frame,angle)
    }

    override fun getRotateFrame(): RectF? = mRotateFrame
    override fun getRotateAnchor(): Point? = mAnchorPoint


    //绘制圆形旋转刻度
    private fun drawRotateMark(canvas: Canvas?, frame: RectF,markAngle: Float){

        mAnchorPoint.set(
            ((frame.left + frame.right) / 2).toInt(),
            (frame.bottom - (frame.right - frame.left) / 2).toInt()
        )
        //计算需要绘制的圆形半径
        mRadius = ((frame.right - frame.left)/2) * cos(Math.toRadians(45.toDouble())) * 2
        //规定需要裁剪的区域范围
        mRotateFrame.set(
            frame.left,
            frame.bottom,
            frame.right,
            ((mRadius - (frame.right - frame.left) / 2)).toFloat() + frame.bottom + 10
        )

        //裁剪画布
        canvas?.clipRect(mRotateFrame)

        canvas?.rotate(markAngle, mAnchorPoint.x.toFloat(), mAnchorPoint.y.toFloat())

        for (index in MAX_ANGLE_VALUE.toInt() downTo MIN_ANGLE_VALUE.toInt() step 2) {
            // 计算刻度位置（从0度开始，按比例分布）
            val angle = (-(index - MAX_ANGLE_VALUE)) / ANGLE_VALUE_RANGE * 180f
//            Log.e("Fizzer", "angle = $angle i = $index")
            val rad = Math.toRadians(angle.toDouble()).toFloat()
            // 计算刻度线的起点和终点
            val startX = mAnchorPoint.x + mRadius * cos(rad)
            val startY = mAnchorPoint.y + mRadius * sin(rad)

            if (index % 10 != 0) {
                canvas?.drawCircle(startX.toFloat(), startY.toFloat(), 2f, textPaint)
            } else {
                canvas?.drawCircle(startX.toFloat(), startY.toFloat(), 6f, textPaint)
            }

            // 每10度绘制一个数字
            if (index % 10 == 0) {
                // 计算文字位置
                val textX = mAnchorPoint.x + (mRadius - 50) * cos(rad)
                val textY = mAnchorPoint.y + (mRadius - 50) * sin(rad) + 10

                // 保存画布状态以绘制不旋转的文字
                canvas?.save()
                // 将文字位置的旋转抵消
                canvas?.rotate(-markAngle, textX.toFloat(), textY.toFloat())
                canvas?.drawText(index.toString(), textX.toFloat(), textY.toFloat(), textPaint)
                canvas?.restore()
            }
        }
    }

}
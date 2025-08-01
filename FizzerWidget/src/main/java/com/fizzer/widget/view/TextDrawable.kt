package com.fizzer.widget.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.widget.TextView

class TextDrawable : Drawable() {

    // 画笔
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.LTGRAY // 默认背景色
        style = Paint.Style.FILL_AND_STROKE
        pathEffect = CornerPathEffect(20f)
    }

    private val marginPx = 25f

    private val mAreaPath = Path()

    val rectList = mutableListOf<RectF>()


    // 持有TextView的引用以获取文字布局信息
    private var textView: TextView? = null

    // 圆角半径
    var cornerRadius = 4f
        set(value) {
            field = value
            invalidateSelf()
        }

    // 背景色
    var backgroundColor: Int
        get() = paint.color
        set(value) {
            paint.color = value
            invalidateSelf()
        }

    fun setTextView(textView: TextView) {
        this.textView = textView
        // 监听文字变化，及时更新绘制
        textView.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                invalidateSelf()
            }

            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    override fun draw(canvas: Canvas) {
        val textView = this.textView ?: return
        val layout = textView.layout ?: return

        // 获取文字总行数
        val lineCount = layout.lineCount
        if (lineCount == 0) return

        // 保存画布状态
        canvas.save()

        // 平移到Drawable的绘制区域
        canvas.translate(25f, 25f)

        // 逐行绘制背景
        for (i in 0 until lineCount) {
            // 获取每行文字的边界
            val lineLeft = layout.getLineLeft(i)
            val lineTop = layout.getLineTop(i)
            val lineRight = layout.getLineRight(i)
            val lineBottom = layout.getLineBottom(i)

            // 创建每行的背景矩形
            val rect = RectF(lineLeft, lineTop.toFloat(), lineRight, lineBottom.toFloat())

            // 绘制圆角矩形背景
            rectList.add(rect)
//            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
        }
        val firstRect = rectList[0]
        val secRect = rectList[1]
        val thRect = rectList[2]
        mAreaPath.apply {
            reset()
            moveTo(firstRect.left - marginPx , firstRect.top - marginPx)
            lineTo(firstRect.right + marginPx, firstRect.top - marginPx)
            lineTo(secRect.right + marginPx,secRect.top)
            lineTo(secRect.right + marginPx,secRect.bottom + marginPx)
            lineTo(thRect.right + marginPx,thRect.top + marginPx)
            lineTo(thRect.right + marginPx,thRect.bottom + marginPx)
            lineTo(thRect.left - marginPx,thRect.bottom + marginPx)
            lineTo(thRect.left- marginPx,thRect.top + marginPx)
            lineTo(secRect.left - marginPx,secRect.bottom + marginPx)
            lineTo(secRect.left - marginPx,secRect.top)
            lineTo(firstRect.left - marginPx,firstRect.bottom)
            close()

        }
        canvas.drawPath(mAreaPath, paint)

        canvas.restore()
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
        invalidateSelf()
    }

    override fun getOpacity(): Int {
        // 根据透明度返回适当的不透明度值
        return when (paint.alpha) {
            255 -> PixelFormat.OPAQUE
            0 -> PixelFormat.TRANSPARENT
            else -> PixelFormat.TRANSLUCENT
        }
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
        invalidateSelf()
    }

    // 重写边界计算，使Drawable大小匹配文字内容
    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        invalidateSelf()
    }

    // 根据文字内容计算Drawable的固有大小
    override fun getIntrinsicWidth(): Int {
        val textView = this.textView ?: return -1
        val layout = textView.layout ?: return -1

        var maxWidth = 0f
        for (i in 0 until layout.lineCount) {
            maxWidth = maxWidth.coerceAtLeast(layout.getLineRight(i) - layout.getLineLeft(i))
        }

        return maxWidth.toInt() + 50
    }

    override fun getIntrinsicHeight(): Int {
        val textView = this.textView ?: return -1
        val layout = textView.layout ?: return -1

        return layout.height + 50
    }


}
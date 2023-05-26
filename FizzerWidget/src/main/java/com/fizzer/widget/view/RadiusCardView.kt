package com.fizzer.widget.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import com.fizzer.widget.R

/**
 * Author:Fizzer
 * Date:10/02/2022
 * Email:Fizzer@miaoshitech.com
 * Description:可以指定CardView的圆角
 */
class RadiusCardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : CardView(context, attrs) {

    private var tlRadius: Float = 0f
    private var trRadius: Float = 0f
    private var blRadius: Float = 0f
    private var brRadius: Float = 0f

    init {
        radius = 0f
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RadiusCardView)
        ta.let {
            tlRadius = it.getDimension(R.styleable.RadiusCardView_rcv_topLeftRadius, 0f)
            trRadius = it.getDimension(R.styleable.RadiusCardView_rcv_topRightRadius, 0f)
            blRadius = it.getDimension(R.styleable.RadiusCardView_rcv_bottomLeftRadius, 0f)
            brRadius = it.getDimension(R.styleable.RadiusCardView_rcv_bottomRightRadius, 0f)
        }
        ta.recycle()
        background = ColorDrawable()
    }

    private fun getRectF(): RectF {
        val rect = Rect()
        getDrawingRect(rect)
        return RectF(rect)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        val path = Path()
        val rectf = getRectF()
        val radius = floatArrayOf(
            tlRadius, tlRadius,
            trRadius, trRadius,
            brRadius, brRadius,
            blRadius, blRadius
        )
        path.addRoundRect(rectf, radius, Path.Direction.CW)
        canvas?.clipPath(path)
        super.onDraw(canvas)
    }
}
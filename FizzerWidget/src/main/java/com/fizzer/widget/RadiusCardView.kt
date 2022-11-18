package com.fizzer.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import androidx.cardview.widget.CardView

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
        var ta = context.obtainStyledAttributes(attrs, R.styleable.RadiusCardView)
        ta.let {
            tlRadius = it.getDimension(R.styleable.RadiusCardView_rcv_topLeftRadiu, 0f)
            trRadius = it.getDimension(R.styleable.RadiusCardView_rcv_topRightRadiu, 0f)
            blRadius = it.getDimension(R.styleable.RadiusCardView_rcv_bottomLeftRadiu, 0f)
            brRadius = it.getDimension(R.styleable.RadiusCardView_rcv_bottomRightRadiu, 0f)
            it.recycle()
        }
        background = ColorDrawable()
    }

    fun getRectF(): RectF {
        var rect = Rect()
        getDrawingRect(rect)
        return RectF(rect)
    }

    override fun onDraw(canvas: Canvas?) {
        var path = Path()
        var rectf = getRectF()
        var radius = floatArrayOf(
            tlRadius, tlRadius,
            trRadius, trRadius,
            brRadius, brRadius,
            blRadius, blRadius
        )
        path.addRoundRect(rectf, radius, Path.Direction.CW)
        canvas?.clipPath(path, Region.Op.INTERSECT)
        super.onDraw(canvas)
    }
}
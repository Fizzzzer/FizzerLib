package com.fizzer.base.lib.ext

import android.graphics.LinearGradient
import android.graphics.Shader
import android.widget.TextView

/**
 * @Author fizzer
 * @Data 2021/6/25 - 12:11 上午
 * @Email Fizzer53@sina.com
 * @Describe:
 */
fun TextView.setGradient(startColor: Int, endColor: Int) {
    var endx = paint.textSize * text.length
    var linearGradient = LinearGradient(
        0f, 0f, endx, 0f,
        startColor, endColor, Shader.TileMode.CLAMP
    )
    paint.shader = linearGradient
    invalidate()
}

fun TextView.setVerticalGradient(startColor: Int, endColor: Int) {
    var endy = lineHeight.toFloat()
    var linearGradient = LinearGradient(
        0f, 0f, 0f, endy,
        startColor, endColor, Shader.TileMode.CLAMP
    )
    paint.shader = linearGradient
    invalidate()
}
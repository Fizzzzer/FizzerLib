package com.fizzer.lib.widget

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import com.fizzer.base.lib.android.act.BaseVBActivity
import com.fizzer.base.lib.ext.clickWithTrigger
import com.fizzer.lib.R
import com.fizzer.lib.databinding.ActivityImageClipLayoutBinding
import kotlin.math.min
import androidx.core.graphics.createBitmap

class ImageClipPage : BaseVBActivity<ActivityImageClipLayoutBinding>() {
    override fun bindingInflate() = ActivityImageClipLayoutBinding.inflate(layoutInflater)

    override fun initView() {
    }

    override fun initEvent() {
        val option = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            BitmapFactory.decodeResource(resources,R.drawable.ic_test,this)
            val reqWidth = 500
            val reqHeight = 500
            inSampleSize = calculateInSampleSize(this,reqWidth,reqHeight)
            inJustDecodeBounds = false
        }
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_test,option)
        binding.imageSrc.setImageBitmap(bitmap)
//        binding.imageSrc.setImageBitmap(
//            cropBitmapWithCanvas(
//                bitmap,
//                0,
//                0,
//                bitmap.width,
//                bitmap.height
//            )
//        )
//        binding.imageSrc.scaleX = 1.5f
//        binding.imageSrc.scaleY = 1.5f
    }

    override fun lazyInitData() {
    }

    private fun cropImage(source: Bitmap, x: Int, y: Int, width: Int, height: Int): Bitmap {
        val cropWidth = min(width, source.width - x)
        val cropHeight = min(height, source.height - y)
        return Bitmap.createBitmap(source, x, y, cropWidth, cropHeight)
    }

    private fun cropBitmapWithCanvas(
        source: Bitmap,
        x: Int,
        y: Int,
        width: Int,
        height: Int
    ): Bitmap {

        val matrix = Matrix().apply {
            setScale(1f, 1f)
//            postRotate(135f, source.width / 2f, source.height / 2f)
        }

        val transformBitmap =
            Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
        return transformBitmap
    }

    // 计算采样比例的辅助方法
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            // 计算最大的 inSampleSize 值，同时保持宽高均大于请求的宽高
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }


}
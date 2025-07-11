package com.fizzer.lib.widget.ImageEditor

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.RectF
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fizzer.base.lib.android.act.BaseVBActivity
import com.fizzer.lib.R
import com.fizzer.lib.databinding.ActivityImageEditorBinding
import com.fizzer.lib.databinding.ActivityItemValuePageBinding
import com.fizzer.lib.widget.ImageEditor.editor.EditMode
import com.github.chrisbanes.photoview.PhotoView

class ImageEditorPage :  AppCompatActivity() {

    private lateinit var zoomImageView: ZoomImageView
    private lateinit var cropOverlayView: CropOverlayView
    private lateinit var btnCrop: Button
    private lateinit var btnReset: Button
    private lateinit var ratioSpinner: Spinner

    private var originalBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_editor)

        // 初始化视图
        zoomImageView = findViewById(R.id.zoomImageView)
        cropOverlayView = findViewById(R.id.cropOverlayView)
        btnCrop = findViewById(R.id.btnCrop)
        btnReset = findViewById(R.id.btnReset)
        ratioSpinner = findViewById(R.id.ratioSpinner)

        // 设置图片
        originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_test)
        zoomImageView.setImageBitmap(originalBitmap!!)

        // 初始化裁剪框
        updateCropOverlayInfo()

        // 图片变化监听
        zoomImageView.onImageChangedListener = {
            updateCropOverlayInfo()
        }

        // 裁剪框变化监听
        cropOverlayView.onCropRectChanged = { rect ->
            // 可以在这里实现实时预览效果
        }

        // 裁剪框调整结束监听
        cropOverlayView.onCropRectFinalized = { rect ->
            // 自动放大图片到裁剪框
            zoomImageView.zoomToRect(rect)
        }

        // 设置比例选择器
        val ratios = CropOverlayView.CropRatio.values()
        val ratioNames = ratios.map { it.display }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ratioNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ratioSpinner.adapter = adapter
        ratioSpinner.setSelection(1) // 默认选择1:1

        // 比例选择监听
        ratioSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                cropOverlayView.setCropRatio(ratios[position].ratio)
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }

        // 裁剪按钮点击事件
        btnCrop.setOnClickListener {
            performCrop()
        }

        // 重置按钮点击事件
        btnReset.setOnClickListener {
            resetView()
        }
    }

    private fun updateCropOverlayInfo() {
        val imageRect = zoomImageView.getImageDrawRect()
        val imageMatrix = zoomImageView.getImageMatrix()
        cropOverlayView.setImageInfo(imageRect, imageMatrix)
    }

    private fun performCrop() {
        originalBitmap?.let { bmp ->
            // 获取裁剪框在屏幕上的坐标
            val cropRect = cropOverlayView.getCropRect()

            // 获取图片变换矩阵的逆矩阵
            val inverseMatrix = Matrix()
            zoomImageView.getImageMatrix().invert(inverseMatrix)

            // 将裁剪框坐标转换为原始图片坐标
            val srcPoints = floatArrayOf(
                cropRect.left, cropRect.top,
                cropRect.right, cropRect.bottom
            )

            val dstPoints = FloatArray(4)
            inverseMatrix.mapPoints(dstPoints, srcPoints)

            val left = max(0f, min(dstPoints[0], dstPoints[2])).toInt()
            val top = max(0f, min(dstPoints[1], dstPoints[3])).toInt()
            val right = min(bmp.width.toFloat(), max(dstPoints[0], dstPoints[2])).toInt()
            val bottom = min(bmp.height.toFloat(), max(dstPoints[1], dstPoints[3])).toInt()

            if (left < right && top < bottom) {
                val cropped = Bitmap.createBitmap(bmp, left, top, right - left, bottom - top)
                // 这里可以保存或显示裁剪后的图片
            }
        }
    }

    private fun resetView() {
        originalBitmap?.let {
            zoomImageView.setImageBitmap(it)
            updateCropOverlayInfo()
            ratioSpinner.setSelection(1)
        }
    }

    private fun max(a: Float, b: Float) = if (a > b) a else b
    private fun min(a: Float, b: Float) = if (a < b) a else b
}
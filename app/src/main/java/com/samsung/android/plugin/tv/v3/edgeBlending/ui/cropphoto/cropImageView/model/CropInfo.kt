package com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto.cropImageView.model

import android.graphics.Bitmap
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto.cropImageView.util.BitmapLoadUtils

class CropInfo(
    val scale: Float,
    val viewBitmapWidth: Float,
    val viewImageTop: Float,
    val viewImageLeft: Float,
    val cropTop: Float,
    val cropLeft: Float,
    val cropWidth: Float,
    val cropHeight: Float
) {
    fun getCroppedImage(path: String?): Bitmap? {
        return getCroppedImage(path, 4000)
    }

    fun getCroppedImage(path: String?, reqSize: Int): Bitmap? {
        val bitmap: Bitmap? = BitmapLoadUtils.decode(path, reqSize, reqSize)
        return bitmap?.let { getCroppedImage(it) }
    }

    fun getCroppedImage(bitmap: Bitmap): Bitmap {
        val scale = scale * (viewBitmapWidth / bitmap.width.toFloat())
        var x = Math.abs(viewImageLeft - cropLeft) / scale
        var y = Math.abs(viewImageTop - cropTop) / scale
        var actualCropWidth = cropWidth / scale
        var actualCropHeight = cropHeight / scale
        if (x < 0) {
            x = 0f
        }
        if (y < 0) {
            y = 0f
        }
        if (y + actualCropHeight > bitmap.height) {
            actualCropHeight = bitmap.height - y
        }
        if (x + actualCropWidth > bitmap.width) {
            actualCropWidth = bitmap.width - x
        }
        return Bitmap.createBitmap(
            bitmap,
            x.toInt(),
            y.toInt(),
            actualCropWidth.toInt(),
            actualCropHeight.toInt()
        )
    }
}
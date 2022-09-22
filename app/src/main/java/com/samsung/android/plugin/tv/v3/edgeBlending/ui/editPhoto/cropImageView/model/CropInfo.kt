package com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.cropImageView.model

import android.graphics.Bitmap
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.cropImageView.util.BitmapLoadUtils
import java.lang.Math.floorDiv
import kotlin.math.abs
import kotlin.math.floor

data class CropInfo(
    val scale: Float,
    val viewBitmapWidth: Float,
    val viewImageTop: Float,
    val viewImageLeft: Float,
    val cropTop: Float,
    val cropLeft: Float,
    val cropWidth: Float,
    val cropHeight: Float
) {

    fun getCroppedImage(path: String?) = getCroppedImage(path, 4000)

    fun getCroppedImage(path: String?, reqSize: Int): Bitmap? {
        val bitmap = BitmapLoadUtils.decode(path, reqSize, reqSize)
        return bitmap?.let { getCroppedImage(it) }
    }

    fun getCroppedImage(bitmap: Bitmap): Bitmap {
        val scale = scale * (viewBitmapWidth / bitmap.width.toFloat())
        var x = abs(viewImageLeft - cropLeft) / scale
        var y = abs(viewImageTop - cropTop) / scale
        var actualCropWidth = cropWidth / scale
        var actualCropHeight = cropHeight / scale
        if (x < 0)  x = 0f
        if (y < 0) y = 0f
        if (y + actualCropHeight > bitmap.height) actualCropHeight = bitmap.height - y
        if (x + actualCropWidth > bitmap.width) actualCropWidth = bitmap.width - x

        return Bitmap.createBitmap(bitmap, x.toInt(), y.toInt(), actualCropWidth.toInt(), actualCropHeight.toInt()
        )
    }
}
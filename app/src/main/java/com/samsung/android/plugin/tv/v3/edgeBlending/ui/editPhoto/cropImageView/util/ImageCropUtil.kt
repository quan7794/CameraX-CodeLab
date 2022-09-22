package com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.cropImageView.util

import android.util.TypedValue
import android.view.View

class ImageCropUtil {
}

fun View.dpToPx(dp: Int)  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()
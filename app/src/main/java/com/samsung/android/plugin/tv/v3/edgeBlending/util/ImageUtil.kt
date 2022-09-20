package com.samsung.android.plugin.tv.v3.edgeBlending.util

import android.content.Context
import android.util.Log
import android.util.TypedValue
import com.samsung.android.architecture.ext.getTagName
import com.samsung.android.architecture.ext.injectObject
import com.samsung.android.plugin.tv.v3.edgeBlending.external.EbLogger

object ImageUtil {
 //  private val logger : EbLogger by injectObject()
   private val TAG: String by getTagName()

    fun getPixels(context: Context, dp: Int): Int {
        val r = context.resources
        val px =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics)
                .toInt()
//       logger.log(
//            Log.DEBUG, TAG, "getPixels()",
//            "dp : $dp, px : $px"
//        )
        return px
    }
}
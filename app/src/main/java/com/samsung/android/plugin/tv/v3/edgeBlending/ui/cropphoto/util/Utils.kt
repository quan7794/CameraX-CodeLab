package com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto.util

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class Utils {
    companion object {
        fun saveImage(bitmap: Bitmap?, directory: File, fileName: String): Boolean {
            bitmap ?: return false
            val file = File(directory, "$fileName.jpg")
            Log.d("createFile", "file out:$directory/$fileName")
            if (file.exists()) {
                file.delete()
            }
            var fileStream: FileOutputStream? = null
            return try {
                fileStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileStream)
            } catch (e: FileNotFoundException) {
                Log.e("createFile", "FileNotFoundException is occurred : " + e.message)
                false
            } finally {
                if (fileStream != null) {
                    try {
                        fileStream.close()
                    } catch (e: IOException) {
                        Log.e("createFile", "Close stream exception is occurred : " + e.message)
                    }
                }
            }
        }
    }
}

fun Context.getCacheImage(no: Int) = "${cacheDir}/EbImage/image$no.jpg"

inline fun View.waitForLayout(crossinline action: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            when {
                viewTreeObserver.isAlive -> {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    action()
                }
                else -> viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
    })
}

fun View.enable(value: Boolean) = run {
    this.isEnabled = value
    this.alpha = if (value) 1f else 0.3f
    this.isClickable = value
}

fun View.setMargin(top: Int = 0, bottom: Int = 0, start: Int = 0, end: Int = 0) {
    val params = this.layoutParams as ViewGroup.MarginLayoutParams
    if (top != 0) params.topMargin = top
    if (bottom != 0) params.bottomMargin = bottom
    if (start != 0) params.marginStart = start
    if (end != 0) params.marginEnd = end
    this.layoutParams = params
}
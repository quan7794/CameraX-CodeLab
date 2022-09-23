package com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.samsung.android.architecture.base.UIState
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.state.ViewOnTvState
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.util.Utils.Companion.EB_CACHE_DIR
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.util.Utils.Companion.EB_IMAGE_FORMAT
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class Utils {
    companion object {
        const val EB_CACHE_DIR = "/EbImage/"
        const val EB_IMAGE_FORMAT = ".jpg"
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

fun Context.getEbCacheImage(name: String) = "${cacheDir}$EB_CACHE_DIR$name$EB_IMAGE_FORMAT"

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

@BindingAdapter("enable")
fun View.enable(value: Boolean) = run {
    this.isEnabled = value
    this.alpha = if (value) 1f else 0.3f
    this.isClickable = value
}

@BindingAdapter("disableWhenViewOnTv")
fun View.disableWhenViewOnTv(value: UIState?) {
    if (value !is ViewOnTvState) return
    this.enable(ViewOnTvState.Running != value)
}

@BindingAdapter("addDecorationWithoutLastDivider")
fun RecyclerView.addDecorationWithoutLastDivider(drawable: Drawable) {
    if (layoutManager !is LinearLayoutManager) return
    val divider = object : DividerItemDecoration(context, (layoutManager as LinearLayoutManager).orientation) {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            if (parent.getChildAdapterPosition(view) == state.itemCount - 1) outRect.setEmpty()
            else super.getItemOffsets(outRect, view, parent, state)
        }
    }
    divider.setDrawable(drawable)
    addItemDecoration(divider)
}

fun View.setMargin(top: Int = 0, bottom: Int = 0, start: Int = 0, end: Int = 0) {
    val params = this.layoutParams as ViewGroup.MarginLayoutParams
    if (top != 0) params.topMargin = top
    if (bottom != 0) params.bottomMargin = bottom
    if (start != 0) params.marginStart = start
    if (end != 0) params.marginEnd = end
    this.layoutParams = params
}
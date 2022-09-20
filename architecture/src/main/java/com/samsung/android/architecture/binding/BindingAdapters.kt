package com.samsung.android.architecture.binding

import android.view.View
import androidx.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.samsung.android.architecture.widgets.decoration.SpaceItemDecoration

class BindingAdapters {

    companion object {

        @JvmStatic
        @BindingAdapter("goneUnless")
        fun goneUnless(view: View, visible: Boolean? = null) {
            view.visibility = if (visible!=null && visible) View.VISIBLE else View.GONE
        }
        @JvmStatic
        @BindingAdapter("visibleUnless")
        fun visibleUnless(view: View, visible: Boolean? = null) {
            view.visibility = if (visible!=null && visible) View.VISIBLE else View.INVISIBLE
        }

        @BindingAdapter("drawable")
        fun setImageDrawable(view: ImageView, drawable: Drawable?) {
            view.setImageDrawable(drawable)
        }

        @JvmStatic
        @BindingAdapter(value=["dividerItemDecoration","itemDecorationBackGround"],requireAll = false)
        fun setDividerItemDecoration(recyclerView: RecyclerView, orientation: Int, background:Drawable) {
            val itemDecoration = DividerItemDecoration(recyclerView.context, orientation)
            background?.let {
                itemDecoration.setDrawable(background)
            }

            recyclerView.addItemDecoration(itemDecoration)
        }

        /**
         * @param recyclerView  RecyclerView to bind to SpaceItemDecoration
         * @param spaceInPx space in pixels
         */
        @JvmStatic
        @BindingAdapter("spaceItemDecoration")
        fun setSpaceItemDecoration(recyclerView: RecyclerView, spaceInPx: Float) {
            if (spaceInPx != 0f) {
                val itemDecoration = SpaceItemDecoration(spaceInPx.toInt(), true, false)
                recyclerView.addItemDecoration(itemDecoration)
            } else {
                val itemDecoration = SpaceItemDecoration(spaceInPx.toInt(), true, false)

                recyclerView.addItemDecoration(itemDecoration)
            }
        }
    }
}
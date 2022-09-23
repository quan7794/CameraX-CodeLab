package com.samsung.android.plugin.tv.v3.edgeBlending.util

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File


class BindingAdapters {

    companion object {


        @JvmStatic
        @BindingAdapter("dividerItemDecoration")
        fun setDividerItemDecoration(recyclerView: RecyclerView, orientation: Int) {
            val itemDecoration = DividerItemDecoration(recyclerView.context, orientation)
            recyclerView.addItemDecoration(itemDecoration)
        }


        @JvmStatic
        @BindingAdapter("srcImageUrl")
        fun setImageSrc(imageView: ImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                Glide.with(imageView.context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView)
            }
        }

        @JvmStatic
        @BindingAdapter("srcImageUrl")
        fun setImageSrc(imageView: ImageView, uri: Uri?) {
            if (uri!= null) {
                Glide.with(imageView.context).load(uri).centerCrop()
                    .priority(Priority.IMMEDIATE).dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(imageView)
//                Glide.with(imageView.context).load(uri).centerCrop().priority(Priority.IMMEDIATE).dontAnimate().diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(object : CustomViewTarget<ImageView?, Drawable?>(imageView) {
//                    override fun onLoadFailed(errorDrawable: Drawable?) {
//                        imageView.setImageDrawable(errorDrawable)
//                    }
//                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
//                        imageView.setImageDrawable(resource.current)
//                    }
//
//                    override fun onResourceCleared(placeholder: Drawable?) {}
//                })
            }
        }

        @JvmStatic
        @BindingAdapter("srcImageAny")
        fun setImageSrcAny(imageView: ImageView, url: Any?) {
            when (url) {
                is String -> {
                    if (!url.isNullOrEmpty()) {
                        Glide.with(imageView.context).load(url)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView)
                    }
                }
                is Int -> {

                    Glide.with(imageView.context).load(url).into(imageView)

                }
            }

        }


        @JvmStatic
        @BindingAdapter("goneUnless")
        fun goneUnless(view: View, visible: Boolean) {
            view.visibility = if (visible) View.VISIBLE else View.GONE
        }



    }

}
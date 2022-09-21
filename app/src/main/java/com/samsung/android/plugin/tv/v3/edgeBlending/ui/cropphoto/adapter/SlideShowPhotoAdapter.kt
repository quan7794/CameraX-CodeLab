package com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto.adapter

import android.net.Uri
import android.view.View
import com.samsung.android.architecture.base.adapter.BaseViewHolder
import com.samsung.android.architecture.base.adapter.SingleLayoutAdapter
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.ItemSimplePhotoBinding

class SlideShowPhotoAdapter(uriList: ArrayList<Uri>, onItemClick: (Int, Uri) -> Unit) :
    SingleLayoutAdapter<Uri, ItemSimplePhotoBinding>(R.layout.item_simple_photo, uriList, onItemClick) {

    var lastSelectedPosition = 0
    var selectedPosition = 0

    override fun onBindViewHolder(holder: BaseViewHolder<Uri, ItemSimplePhotoBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding.selectedLine.visibility = if (selectedPosition == position) View.VISIBLE else View.GONE
    }

//    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int, newItems: List<CropPhotoModel>): Boolean {
//        return items[oldItemPosition].uri == newItems[newItemPosition].uri
//    }
//
//    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int, newItems: List<CropPhotoModel>): Boolean {
//        return items[oldItemPosition].isSelected == newItems[newItemPosition].isSelected
//    }

}
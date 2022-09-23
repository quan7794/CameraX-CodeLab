package com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.adapter

import android.net.Uri
import android.view.View
import com.samsung.android.architecture.base.adapter.BaseViewHolder
import com.samsung.android.architecture.base.adapter.SingleLayoutAdapter
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.ItemSimplePhotoBinding

class SelectedPhotosAdapter(uriList: ArrayList<Uri>, onItemClick: (Int, Uri) -> Unit) :
    SingleLayoutAdapter<Uri, ItemSimplePhotoBinding>(R.layout.item_simple_photo, uriList, onItemClick) {

    var recentPosition = 0
    var currentPosition = 0

    override fun onBindViewHolder(holder: BaseViewHolder<Uri, ItemSimplePhotoBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding.selectLine.visibility = if (currentPosition == position) View.VISIBLE else View.GONE
    }

//    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int, newItems: List<CropPhotoModel>): Boolean {
//        return items[oldItemPosition].uri == newItems[newItemPosition].uri
//    }
//
//    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int, newItems: List<CropPhotoModel>): Boolean {
//        return items[oldItemPosition].isSelected == newItems[newItemPosition].isSelected
//    }

}
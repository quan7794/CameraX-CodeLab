package com.samsung.android.plugin.tv.v3.edgeBlending.adapter

import android.view.View
import com.samsung.android.architecture.base.adapter.BaseViewHolder
import com.samsung.android.architecture.base.adapter.SingleLayoutAdapter
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.ItemFreestyleDeviceBinding
import com.samsung.android.plugin.tv.v3.edgeBlending.domain.model.EbDevice
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.searchdevice.EBDeviceModel

class EBDeviceAdapter( onItemClick: (Int, EBDeviceModel) -> Unit): SingleLayoutAdapter<EBDeviceModel, ItemFreestyleDeviceBinding>(R.layout.item_freestyle_device, emptyList(), onItemClick) {

    override fun onBindViewHolder(holder: BaseViewHolder<EBDeviceModel, ItemFreestyleDeviceBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (position == items.size -1) {
            holder.binding.viewDivider.visibility = View.GONE
        } else {
            holder.binding.viewDivider.visibility = View.VISIBLE
        }
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
        newItems: List<EBDeviceModel>
    ): Boolean {
        return items[oldItemPosition].isSelected == newItems[newItemPosition].isSelected
    }
}
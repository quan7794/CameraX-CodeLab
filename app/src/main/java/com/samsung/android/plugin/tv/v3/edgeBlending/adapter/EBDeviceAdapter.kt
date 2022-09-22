package com.samsung.android.plugin.tv.v3.edgeBlending.adapter

import com.samsung.android.architecture.base.adapter.SingleLayoutAdapter
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.ItemFreestyleDeviceBinding
import com.samsung.android.plugin.tv.v3.edgeBlending.domain.model.EbDevice
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.searchdevice.EBDeviceModel

class EBDeviceAdapter(private val onItemClick: (Int, EBDeviceModel) -> Unit): SingleLayoutAdapter<EBDeviceModel, ItemFreestyleDeviceBinding>(R.layout.item_freestyle_device, emptyList(), onItemClick) {
}
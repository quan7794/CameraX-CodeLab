package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.searchdevice

import com.samsung.android.plugin.tv.v3.edgeBlending.domain.model.EbDevice

data class EBDeviceModel(val model:EbDevice, val isSelected: Boolean = false) {
    override fun toString(): String {
        return model.deviceId
    }
}
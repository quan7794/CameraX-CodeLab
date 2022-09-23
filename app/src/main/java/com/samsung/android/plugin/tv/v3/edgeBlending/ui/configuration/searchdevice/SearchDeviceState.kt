package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.searchdevice

import com.samsung.android.architecture.base.UIState

sealed class SearchDeviceState: UIState {
    class SelectedDevice(val devices:MutableList<EBDeviceModel>): SearchDeviceState()
    class SearchResult(val devices:List<EBDeviceModel>): SearchDeviceState()
}
package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.searchdevice

import com.samsung.android.architecture.base.UIState
import com.samsung.android.plugin.tv.v3.edgeBlending.domain.model.EbDevice

sealed class SearchDeviceEvent: UIState {
    class ON_PREVIOUS_CLICK(val ebDevice: EbDevice?): SearchDeviceEvent()
    class ON_NEXT_CLICK(val ebDevice: EbDevice?): SearchDeviceEvent()
}
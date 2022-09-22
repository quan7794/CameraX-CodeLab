package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.connectdevice

import com.samsung.android.architecture.base.UIState
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.searchdevice.EBDeviceModel

sealed class ConnectDeviceState: UIState {
    class SetDevice(val device: EBDeviceModel): ConnectDeviceState()
}
package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.connectdevice

import com.samsung.android.architecture.base.UIState

sealed class ConnectDeviceEvent: UIState {
    object onSelectDevice: ConnectDeviceEvent()
}
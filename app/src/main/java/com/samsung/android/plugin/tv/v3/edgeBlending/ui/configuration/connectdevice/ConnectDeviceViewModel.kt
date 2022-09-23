package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.connectdevice

import com.samsung.android.architecture.base.BaseViewModel
import com.samsung.android.plugin.tv.v3.edgeBlending.domain.model.EbDevice

class ConnectDeviceViewModel: BaseViewModel() {

    private var selectedDevice: EbDevice? = null

    fun onSelectDevice() {
      _clickEvent.value = ConnectDeviceEvent.onSelectDevice(selectedDevice?.deviceId)
    }

    fun setDevice(ebDevice: EbDevice?) {
        _state.value = ConnectDeviceState.SetDevice(ebDevice)
        selectedDevice = ebDevice
    }

}
package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.connectdevice

import com.samsung.android.architecture.base.BaseViewModel

import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.searchdevice.EBDeviceModel

class ConnectDeviceViewModel: BaseViewModel() {



    fun onSelectDevice() {
      _clickEvent.value = ConnectDeviceEvent.onSelectDevice
    }

    fun setDevice(ebDevice: EBDeviceModel) {
        _state.value = ConnectDeviceState.SetDevice(ebDevice)
    }

}
package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.searchdevice


import androidx.appcompat.widget.SearchView
import androidx.lifecycle.viewModelScope
import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.db.api.DeviceRepository
import com.samsung.android.plugin.tv.v3.edgeBlending.domain.model.EbDevice
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.viewmodel.NavigateUpViewModel
import com.samsung.android.plugin.tv.v3.edgeBlending.util.getQueryTextChangeStateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

import kotlinx.coroutines.launch

class SearchDeviceViewModel(val deviceRepository: DeviceRepository): NavigateUpViewModel() {
    val deviceList: MutableList<EBDeviceModel> = ArrayList()

    private var prevPosition: Int = 0
    private var selectedDevice: EBDeviceModel? = null

    init {
        showHeader(true)
    }

    fun setPhotoSelected(position: Int, photo: EBDeviceModel, adapterList:MutableList<EBDeviceModel>) {

        val selected = !photo.isSelected
        val newList = adapterList.toMutableList()

        newList[position] = newList[position].copy(isSelected = selected)
        if (selected) {
            if (prevPosition != position) {
                newList[prevPosition] = newList[prevPosition].copy(isSelected = false)
                prevPosition = position
            }
            selectedDevice = newList[position]

        } else {
            selectedDevice = null
        }
        _state.value = SearchDeviceState.SelectedDevice(newList)
    }

    fun getNearbyDevices(prevDeviceId: String?){
        val devices = fakeNearByDevices()
       if(prevDeviceId != null) {
            fakeNearByDevices().forEachIndexed { index, ebDeviceModel ->
                if (ebDeviceModel.model.deviceId == prevDeviceId) {
                    devices[index] = devices[index].copy(isSelected = true)
                    prevPosition = index
                    return@forEachIndexed
                }
             }
        }
        deviceList.addAll(devices)
    }

    private fun fakeNearByDevices(): MutableList<EBDeviceModel>{
        val devices: MutableList<EBDeviceModel> = ArrayList()
        val device = EbDevice("FreeStyle A","abcd","abcd","12345", available = true, connected = false)
        devices.add(EBDeviceModel(device))
        devices.add(EBDeviceModel(device.copy(name = "FreeStyle B", deviceId = "1", available = false)))
        devices.add(EBDeviceModel(device.copy(name = "FreeStyle C", deviceId = "2", connected = true)))
        devices.add(EBDeviceModel(device.copy(name = "FreeStyle D", deviceId = "3")))
        devices.add(EBDeviceModel(device.copy(name = "FreeStyle E", deviceId = "4")))
        devices.add(EBDeviceModel(device.copy(name = "FreeStyle F", deviceId = "5")))
        devices.add(EBDeviceModel(device.copy(name = "FreeStyle G", deviceId = "6")))
        devices.add(EBDeviceModel(device.copy(name = "FreeStyle H", deviceId = "7")))
        return devices
    }


   fun setUpSearchStateFlow(searchView: SearchView) {
        viewModelScope.launch {
            searchView.getQueryTextChangeStateFlow()
                .debounce(300)
                .filter { query ->
                    if (query.isEmpty()) {
                       _state.postValue(SearchDeviceState.SearchResult(deviceList))
                        return@filter false
                    } else {
                        return@filter true
                    }
                }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                  filterDevice(query)
                }
                .flowOn(Dispatchers.Default)
                .collect { result ->
                   _state.postValue(SearchDeviceState.SearchResult(result))
                }
        }
    }

    private fun filterDevice(query: String): Flow<List<EBDeviceModel>> {
        return flow {
           emit(deviceList.filter { it.model.name.contains(query, true) })

        }
    }

    fun onNextClick() {
        _clickEvent.value = SearchDeviceEvent.ON_NEXT_CLICK(selectedDevice?.model)
    }

    fun onPreviousClick() {
       _clickEvent.value = SearchDeviceEvent.ON_PREVIOUS_CLICK(selectedDevice?.model)
    }

    override fun onBackClick() {
        super.onBackClick()
        _clickEvent.value = SearchDeviceEvent.ON_PREVIOUS_CLICK(selectedDevice?.model)
    }

}
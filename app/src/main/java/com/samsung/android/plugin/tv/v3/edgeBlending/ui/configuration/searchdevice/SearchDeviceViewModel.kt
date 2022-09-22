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

    init {
        showHeader(true)
    }

    fun setPhotoSelected(position: Int, photo: EBDeviceModel, adapterList:MutableList<EBDeviceModel>) {
        val selected = !photo.isSelected
        val newList = adapterList.toMutableList()
        newList[position] = newList[position].copy(isSelected = selected)
        _state.value = SearchDeviceState.SelectedDevice(newList)
    }

    fun getNearbyDevices(){
        deviceList.addAll(fakeNearByDevices())
    }

    private fun fakeNearByDevices(): MutableList<EBDeviceModel>{
        val devices: MutableList<EBDeviceModel> = ArrayList()
        val device = EbDevice("FreeStyle A","abcd","abcd","12345", available = true, connected = false)
        devices.add(EBDeviceModel(device))
        devices.add(EBDeviceModel(device.copy(name = "FreeStyle B", available = false)))
        devices.add(EBDeviceModel(device.copy(name = "FreeStyle C", connected = true)))
        devices.add(EBDeviceModel(device.copy(name = "FreeStyle D")))
        devices.add(EBDeviceModel(device.copy(name = "FreeStyle E")))
        devices.add(EBDeviceModel(device.copy(name = "FreeStyle F")))
        devices.add(EBDeviceModel(device.copy(name = "FreeStyle G")))
        devices.add(EBDeviceModel(device.copy(name = "FreeStyle H")))
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

    }

    fun onPreviousClick() {

    }

    override fun onBackClick() {
        super.onBackClick()
    }

}
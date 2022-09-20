package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuraion


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.samsung.android.architecture.base.BaseViewModel

import androidx.lifecycle.viewModelScope
import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.preferences.api.DatastoreRepository
import kotlinx.coroutines.launch


class ConfigurationViewModel(val dataStore: DatastoreRepository): BaseViewModel() {
    val _isChecked = MutableLiveData<Boolean>()
    val isChecked: LiveData<Boolean> = _isChecked


    init {
        viewModelScope.launch {
            _isChecked.value = dataStore.isHideConfigScreen()
        }
    }
    fun onStartClick(){

    }
    fun onCheckedChange(check: Boolean) {

        viewModelScope.launch {
            dataStore.hideConfigScreen(check)
        }

    }

}
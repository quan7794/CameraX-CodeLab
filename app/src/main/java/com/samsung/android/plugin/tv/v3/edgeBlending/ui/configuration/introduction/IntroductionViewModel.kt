package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.introduction


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.samsung.android.architecture.base.BaseViewModel

import androidx.lifecycle.viewModelScope
import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.preferences.api.DatastoreRepository
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.viewmodel.NavigateUpViewModel
import kotlinx.coroutines.launch


class IntroductionViewModel(val dataStore: DatastoreRepository): NavigateUpViewModel() {
    val _isChecked = MutableLiveData<Boolean>()
    val isChecked: LiveData<Boolean> = _isChecked


    init {
        viewModelScope.launch {
            _isChecked.value = dataStore.isHideConfigScreen()
        }
        showHeader(true)
    }
    fun onStartClick(){
     _clickEvent.value = IntroductionEvent.OnStartClick
    }
    fun onCheckedChange(check: Boolean) {

        viewModelScope.launch {
            dataStore.hideConfigScreen(check)
        }

    }

    override fun onBackClick() {
        super.onBackClick()
    }

}
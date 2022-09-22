package com.samsung.android.plugin.tv.v3.edgeBlending.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.samsung.android.architecture.base.BaseViewModel
import com.samsung.android.architecture.base.UIState

open class NavigateUpViewModel: BaseViewModel() {

    private val _showHeader = MutableLiveData<Boolean>()
    val showHeader: LiveData<Boolean>
        get() = _showHeader

    fun showHeader(isShow: Boolean) {
        _showHeader.value = isShow
    }
    open fun onBackClick(){}
}
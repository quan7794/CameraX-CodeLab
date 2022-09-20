package com.samsung.android.architecture.event.activity

import com.samsung.android.architecture.base.BaseViewModel
class UpdateMainUIViewModel: BaseViewModel() {

    fun hideHeaderView(isHide:Boolean = true) {
        _state.value = MainUIState.HideHeaderView(isHide)
    }
}
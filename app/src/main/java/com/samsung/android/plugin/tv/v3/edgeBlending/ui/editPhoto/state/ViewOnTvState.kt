package com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.state

import com.samsung.android.architecture.base.UIState

sealed class ViewOnTvState: UIState {
    object Running: ViewOnTvState()
    object Success : ViewOnTvState()
    object Cancel : ViewOnTvState()
    class Error(val cause: String) : ViewOnTvState()
}
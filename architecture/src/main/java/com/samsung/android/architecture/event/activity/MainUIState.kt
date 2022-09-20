package com.samsung.android.architecture.event.activity

import com.samsung.android.architecture.base.UIState

class MainUIState {
    class HideHeaderView(val isHide:Boolean = false): UIState
}
package com.samsung.android.architecture.library.pickimage.state

import com.samsung.android.architecture.base.UIState

sealed class StartMode: UIState {
    object NORMAL: UIState
    object CHANGE: UIState
    object RESELECT: UIState
    object SHORTCUT: UIState
}


package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.introduction

import com.samsung.android.architecture.base.UIState

sealed class IntroductionEvent: UIState {
    object OnStartClick: IntroductionEvent()
}
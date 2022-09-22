package com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.state

import com.samsung.android.architecture.base.UIState

sealed class EditPhotoState:UIState {
    object ClickCropPhoto : EditPhotoState()
    object ClickViewOnTv : EditPhotoState()
    object ClickSetting : EditPhotoState()
}
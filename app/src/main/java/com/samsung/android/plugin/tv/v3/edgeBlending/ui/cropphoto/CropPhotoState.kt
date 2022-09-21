package com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto

import android.net.Uri
import com.samsung.android.architecture.base.UIState

sealed class CropPhotoState:UIState {
    class SelectedPhoto(val list: List<Uri>): CropPhotoState()
    object CropState : CropPhotoState()
}
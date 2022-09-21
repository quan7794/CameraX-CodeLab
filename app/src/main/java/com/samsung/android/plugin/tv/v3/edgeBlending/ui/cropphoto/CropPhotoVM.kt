package com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto

import android.net.Uri
import android.view.View
import com.samsung.android.architecture.base.BaseViewModel
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class CropPhotoVM: BaseViewModel() {
    var uriList = arrayListOf<Uri>()
    val viewStateList = mutableMapOf<Int, FloatArray>()

    private var _slideShowVisibility = MutableStateFlow(View.GONE)
    val slideShowVisibility = _slideShowVisibility.asStateFlow()

    fun setSlideVisibility(isVisible: Boolean) {
        _slideShowVisibility.value = if (isVisible) View.VISIBLE else View.GONE
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnCrop -> {_state.value = CropPhotoState.CropState}
            R.id.btnSlideShowSetting -> {}
            R.id.btnViewOnTv -> {}
        }
    }

}
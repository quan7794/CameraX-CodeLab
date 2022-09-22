package com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto

import android.net.Uri
import android.view.View
import com.samsung.android.architecture.base.BaseViewModel
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.state.EditPhotoState
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.state.ViewOnTvState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class EditPhotoViewModel: BaseViewModel() {
    var uriList = arrayListOf<Uri>()
    val cropStateList = mutableMapOf<Int, FloatArray>()

    private var _slideShowVisibility = MutableStateFlow(View.GONE)
    val slideShowVisibility = _slideShowVisibility.asStateFlow()

    fun setSlideVisibility(isVisible: Boolean) {
        _slideShowVisibility.value = if (isVisible) View.VISIBLE else View.GONE
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnCrop -> {_clickEvent.value = EditPhotoState.ClickCropPhoto}
            R.id.btnSlideShowSetting -> { _clickEvent.value = EditPhotoState.ClickSetting}
            R.id.btnViewOnTv -> { _clickEvent.value = EditPhotoState.ClickViewOnTv}
        }
    }

    fun updateUiState(state: ViewOnTvState) = _state.postValue(state)
}
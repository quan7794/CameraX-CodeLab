package com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.samsung.android.architecture.base.BaseViewModel


class CropPhotoVM: BaseViewModel() {
    var uriList = arrayListOf<Uri>()
}
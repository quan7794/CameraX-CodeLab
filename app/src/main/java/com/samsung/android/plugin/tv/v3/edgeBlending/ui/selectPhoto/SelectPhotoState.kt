package com.samsung.android.plugin.tv.v3.edgeBlending.ui.selectPhoto

import android.net.Uri
import com.samsung.android.architecture.base.UIState
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.selectPhoto.model.PhotoModel
import java.net.URI

sealed class SelectPhotoState: UIState {
    class GetAlbum(val albumName: String, val photoOfAlbum: List<PhotoModel.Data>): UIState
    class GetAlbumNames(val albumNames: MutableList<String>): UIState
    class SelectedPhoto(val selectedImageList: List<PhotoModel.Data>,
                        val numPhotoSelected:Int = 0): UIState
    class GotoCropPhotoScreen(val uris: ArrayList<Uri>): UIState
    object AlbumClick: SelectPhotoState()

}
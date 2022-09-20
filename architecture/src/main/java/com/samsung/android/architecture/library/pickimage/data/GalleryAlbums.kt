package com.samsung.android.architecture.library.pickimage.data

import android.net.Uri

data class GalleryAlbums(
    var id: Long? = 0,
    var name: String? = "",
    var coverUri: Uri? = null,
    var albumPhotos: MutableList<GalleryData> = ArrayList()
)
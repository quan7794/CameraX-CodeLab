package com.samsung.android.architecture.library.pickimage.data

import android.net.Uri

data class GalleryData(
    val uri: Uri,
    val dateTaken: Long?,
    val displayName: String?,
    val id: Long?,
    val folderName: String?,
    var albumId: Long? = 0
)

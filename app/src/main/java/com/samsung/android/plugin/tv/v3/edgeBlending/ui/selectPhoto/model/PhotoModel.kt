package com.samsung.android.plugin.tv.v3.edgeBlending.ui.selectPhoto.model


import android.net.Uri
import com.samsung.android.architecture.base.adapter.ItemComparable
import com.samsung.android.architecture.base.adapter.ItemSeperator
import com.samsung.android.architecture.library.pickimage.data.GalleryData

sealed class PhotoModel: ItemComparable {
    data class Data(val id: Long?,
                    val albumId:Long?,
                    val uri: Uri,
                    var isSelected: Boolean = false,
                    val folderName: String?,
                    var numSelected: Int = 0,
                    var isEnabled: Boolean = true)
    class Seperator(val tag: String) : PhotoModel(), ItemSeperator

}
package com.samsung.android.architecture.library.pickimage.state

import com.samsung.android.architecture.base.UIState

sealed class ViewModeState: UIState {
    object GALLERY: UIState
    object SCLOUD_PHOTO: UIState
    object SCLOUD_ALBUM: UIState
    object SCLOUD_ALBUM_PHOTO: UIState
    object SCLOUD_STORY: UIState
    object SCLOUD_STORY_PHOTO: UIState
    object SCLOUD_SHARED: UIState
}


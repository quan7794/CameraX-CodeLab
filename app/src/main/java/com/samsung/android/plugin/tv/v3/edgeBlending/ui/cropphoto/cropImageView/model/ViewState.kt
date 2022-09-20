package com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto.cropImageView.model

import android.graphics.Matrix

class ViewState(val matrix: Matrix, val suppMatrixValues: FloatArray) {


}

fun MutableList<ViewState>.addOrUpdate(index: Int, viewState: ViewState) {
    if (this.getOrNull(index) == null) this.add(index, viewState)
    else this[index] = viewState
}
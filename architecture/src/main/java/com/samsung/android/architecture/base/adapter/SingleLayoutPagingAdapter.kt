package com.samsung.android.architecture.base.adapter

import androidx.databinding.ViewDataBinding

open class SingleLayoutPagingAdapter<T : ItemComparable, B : ViewDataBinding>(
    private val layoutId: Int,
    onItemClicked: ((Int,(T)) -> Unit)? = null,
    onBind: B.(Int) -> Unit = {}
) : BasePagingAdapter<T, B>(onItemClicked = onItemClicked, onBind = onBind) {

    override fun getLayoutId(position: Int): Int = layoutId
}
package com.samsung.android.architecture.ext

import com.samsung.android.architecture.base.BaseViewModel
import com.samsung.android.architecture.base.BaseVmFragment

fun BaseVmFragment<*>.logD(func: String, msg: String = "Entry") {
    logger?.d(this::class.java.simpleName, func, msg)
}

fun BaseVmFragment<*>.logV(func: String, msg: String = "Entry") {
    logger?.v(this::class.java.simpleName, func, msg)
}


fun BaseViewModel.logD(func: String, msg: String = "Entry") {
    logger?.d(this::class.java.simpleName, func, msg)
}

fun BaseViewModel.logW(func: String, msg: String = "Entry") {
    logger?.w(this::class.java.simpleName, func, msg)
}

fun BaseViewModel.logE(func: String, msg: String = "Entry") {
    logger?.e(this::class.java.simpleName, func, msg)
}

fun BaseVmFragment<*>.logE(func: String, msg: String = "Entry") {
    logger?.e(this::class.java.simpleName, func, msg)
}

fun BaseVmFragment<*>.logI(func: String, msg: String = "Entry") {
    logger?.i(this::class.java.simpleName, func, msg)
}

fun BaseViewModel.logI(func: String, msg: String = "Entry") {
    logger?.i(this::class.java.simpleName, func, msg)
}

fun BaseVmFragment<*>.logW(func: String, msg: String = "Entry") {
    logger?.w(this::class.java.simpleName, func, msg)
}

package com.samsung.android.architecture.ext

inline fun  Any.getTagName(): Lazy<String> = lazy { this::class.java.simpleName }

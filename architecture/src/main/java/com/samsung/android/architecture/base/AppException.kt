package com.samsung.android.architecture.base

import java.lang.Exception

open class AppException(open var code: Int, override val message: String): Exception(message)
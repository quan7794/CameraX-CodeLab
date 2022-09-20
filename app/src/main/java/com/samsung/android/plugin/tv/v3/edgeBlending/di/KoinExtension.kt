package com.samsung.android.plugin.tv.v3.edgeBlending.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import org.koin.android.compat.ScopeCompat


import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

import org.koin.core.Koin
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent


const val ID_SCOPE_TV_V3 = "tv.v3"

private const val TAG = "KoinExtension"
fun Context.startKoin(): Koin {
    return try {
        startKoinForV3()
    } catch (e: Exception) {

        stopKoin()
        startKoinForV3()
    }
}

private fun Context.startKoinForV3(): Koin {
    return org.koin.core.context.startKoin {

        androidLogger(Level.ERROR)
        androidContext(this@startKoinForV3)

    }.koin
}



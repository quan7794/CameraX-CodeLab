package com.samsung.android.plugin.tv.v3.edgeBlending.di

import androidx.fragment.app.FragmentActivity
import com.samsung.android.plugin.tv.v3.edgeBlending.external.EbLogger


object EbInjector {
    @JvmField
    val TAG: String = EbInjector::class.java.simpleName

    private var mLogger: EbLogger? = null




    @JvmStatic
    fun setEbLogger(seroLogger: EbLogger) {
        mLogger = seroLogger
    }


    @Synchronized
    @JvmStatic
    fun getEbLogger() = checkNotNull(mLogger)

    @JvmStatic
    fun clear() {
        mLogger = null
    }

    @JvmStatic
    fun isInitialized(): Boolean {
        return mLogger != null
    }
}
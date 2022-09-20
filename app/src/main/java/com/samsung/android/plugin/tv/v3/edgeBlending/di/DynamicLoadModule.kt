package com.samsung.android.plugin.tv.v3.edgeBlending.di



import org.koin.core.Koin

class DynamicLoadModule(val koin:Koin) {


    val loadEBModule by lazy {

        koin.loadModules(listOf(ebModule))

    }


    fun unloadAll() {
        koin.unloadModules(listOf( ebModule))
    }
}
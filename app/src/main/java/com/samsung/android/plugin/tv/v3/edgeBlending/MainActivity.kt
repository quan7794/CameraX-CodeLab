package com.samsung.android.plugin.tv.v3.edgeBlending


import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import com.samsung.android.architecture.ext.replaceFragment
import com.samsung.android.plugin.tv.v3.edgeBlending.di.DynamicLoadModule
import com.samsung.android.plugin.tv.v3.edgeBlending.di.EbInjector
import com.samsung.android.plugin.tv.v3.edgeBlending.di.startKoin
import com.samsung.android.plugin.tv.v3.edgeBlending.external.EbLoggerImpl
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.selectPhoto.SelectPhotoFragment


open class MainActivity : AppCompatActivity() {
    protected val koin by lazy { startKoin() }

    val dynamicLoadModule: DynamicLoadModule by lazy { DynamicLoadModule(koin) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
               initKoin()
        replaceFragment(SelectPhotoFragment(),R.id.container,SelectPhotoFragment.Tag)


    }

    private fun initKoin() {
        koin
        EbInjector.setEbLogger(EbLoggerImpl())
        dynamicLoadModule.loadEBModule

    }


    override fun onDestroy() {
        super.onDestroy()
        dynamicLoadModule.unloadAll()
    }


}






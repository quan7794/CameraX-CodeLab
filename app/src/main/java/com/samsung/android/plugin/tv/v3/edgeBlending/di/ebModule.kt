package com.samsung.android.plugin.tv.v3.edgeBlending.di

import com.samsung.android.architecture.base.ILogger

import org.koin.core.qualifier.named
import org.koin.dsl.module

val ebModule = module {
    scope(named(ID_SCOPE_TV_V3)) {
        scoped<ILogger>{
             EbInjector.getEbLogger()
         }

    }

}

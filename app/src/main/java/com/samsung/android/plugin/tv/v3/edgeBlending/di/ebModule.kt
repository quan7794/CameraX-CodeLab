package com.samsung.android.plugin.tv.v3.edgeBlending.di

import com.samsung.android.architecture.base.ILogger
import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.preferences.api.DatastoreRepository
import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.preferences.impl.DataStore
import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.preferences.impl.DatastoreRepositoryImpl
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuraion.ConfigurationViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel

import org.koin.core.qualifier.named
import org.koin.dsl.module

val ebModule = module {
    scope(named(ID_SCOPE_TV_V3)) {
        scoped<ILogger>{
             EbInjector.getEbLogger()
         }
        scoped<DatastoreRepository>{ DatastoreRepositoryImpl(DataStore(androidContext())) }
        viewModel {
            ConfigurationViewModel(get())
        }
    }

}

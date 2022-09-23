package com.samsung.android.plugin.tv.v3.edgeBlending.di

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.room.Room
import com.samsung.android.architecture.base.ILogger
import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.db.EBDatabase
import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.db.EBDatabaseConstants
import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.db.api.DeviceRepository
import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.db.dao.DeviceDao
import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.db.impl.DeviceRepositoryImpl
import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.preferences.api.DatastoreRepository
import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.preferences.impl.DataStore
import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.preferences.impl.DatastoreRepositoryImpl
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.connectdevice.ConnectDeviceViewModel
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.introduction.IntroductionViewModel
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.searchdevice.SearchDeviceViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf

import org.koin.core.qualifier.named
import org.koin.dsl.module

val ebModule = module {
    scope(named(ID_SCOPE_TV_V3)) {
        scoped<ILogger>{
             EbInjector.getEbLogger()
         }

        scoped {
            Room.databaseBuilder(androidContext(), EBDatabase::class.java, "${EBDatabaseConstants.DATABASE_NAME}")
                .fallbackToDestructiveMigration()
                .build()
                .deviceDao()

        }
        scoped<DatastoreRepository>{ DatastoreRepositoryImpl(DataStore(androidContext())) }

        scoped<DeviceRepository>{
            DeviceRepositoryImpl(get())
        }
        viewModel {
            IntroductionViewModel(get())
        }

        viewModel {
            SearchDeviceViewModel(get())
        }
        viewModel {
            ConnectDeviceViewModel()
        }
    }

}

package com.samsung.android.plugin.tv.v3.edgeBlending.datasource.preferences.impl

import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.preferences.api.DatastoreRepository


class DatastoreRepositoryImpl(private val dataStore: DataStore): DatastoreRepository {

    override suspend fun hideConfigScreen(isHide: Boolean) {
        dataStore.saveConfig(isHide)
    }

    override suspend fun isHideConfigScreen(): Boolean = dataStore.isHideConfig()

}
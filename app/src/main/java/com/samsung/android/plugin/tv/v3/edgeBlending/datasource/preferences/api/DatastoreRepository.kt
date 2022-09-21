package com.samsung.android.plugin.tv.v3.edgeBlending.datasource.preferences.api

interface DatastoreRepository {
    suspend fun hideConfigScreen(isHide: Boolean)
    suspend fun isHideConfigScreen(): Boolean

}
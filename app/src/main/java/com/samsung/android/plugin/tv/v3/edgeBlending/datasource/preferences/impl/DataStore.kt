package com.samsung.android.plugin.tv.v3.edgeBlending.datasource.preferences.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "eb_datastore")

class DataStore(private val context: Context) {

    private val HIDE_CONFIG_INTRODUTION = booleanPreferencesKey("hide_config")

    private suspend fun <T> save(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { settings ->
            settings[key] = value
        }
    }


    private suspend fun <T> get(key: Preferences.Key<T>): T? =
        context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[key]
            }
            .first()




    suspend fun isHideConfig(): Boolean = get(HIDE_CONFIG_INTRODUTION)?: false

    suspend fun saveConfig(isShow: Boolean) {
        save(HIDE_CONFIG_INTRODUTION, isShow)
    }


}
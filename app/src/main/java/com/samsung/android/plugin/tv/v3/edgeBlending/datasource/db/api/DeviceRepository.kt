package com.samsung.android.plugin.tv.v3.edgeBlending.datasource.db.api

import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.db.models.Device


interface DeviceRepository {
    suspend fun addDevice(device: Device)
    suspend fun updateDevice(connected: Boolean, available: Boolean)
    suspend fun getDevices(): List<Device>
    suspend fun getDevice(name: String): Device
}
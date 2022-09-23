package com.samsung.android.plugin.tv.v3.edgeBlending.datasource.db.impl


import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.db.api.DeviceRepository
import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.db.dao.DeviceDao
import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.db.models.Device
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

 class DeviceRepositoryImpl (private val deviceDao: DeviceDao) : DeviceRepository {
    override suspend fun addDevice(device: Device) {
        withContext(Dispatchers.IO) {
            deviceDao.insertDevice(device)
        }
    }

    override suspend fun updateDevice(connected: Boolean, available: Boolean) {
        withContext(Dispatchers.IO) {
            deviceDao.update(connected, available)
        }
    }

    override suspend fun getDevices(): List<Device>  = deviceDao.getDevices()
    override suspend fun getDevice(name: String): Device = deviceDao.getDevice(name)
}
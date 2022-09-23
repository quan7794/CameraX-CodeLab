package com.samsung.android.plugin.tv.v3.edgeBlending.datasource.db.mappers


import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.db.models.Device
import com.samsung.android.plugin.tv.v3.edgeBlending.domain.model.EbDevice
import java.util.*

fun EbDevice.toDbEntity() =
    Device(
        id = deviceId,
        name = name,
        ip = ip,
        macAddress = macAddress,
        connected = connected,
        available = available,
        updatedAt = Date().time)


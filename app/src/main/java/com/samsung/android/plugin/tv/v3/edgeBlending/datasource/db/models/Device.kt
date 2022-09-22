package com.samsung.android.plugin.tv.v3.edgeBlending.datasource.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class Device(
    @PrimaryKey
    val id: String,
    val name: String?,
    val ip: String,
    val macAddress: String?,
    val available: Boolean = true,
    val connected: Boolean = false,
    val updatedAt: Long?
)
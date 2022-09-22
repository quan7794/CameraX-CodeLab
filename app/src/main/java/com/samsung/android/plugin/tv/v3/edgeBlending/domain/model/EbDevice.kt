package com.samsung.android.plugin.tv.v3.edgeBlending.domain.model

import com.google.gson.annotations.SerializedName

data class EbDevice(
    @SerializedName("name") val name: String,
    @SerializedName("macAddress") val macAddress: String,
    @SerializedName("ip") val ip: String,
    @SerializedName("deviceId") val deviceId: String,
    val connected: Boolean,
    val available: Boolean
)

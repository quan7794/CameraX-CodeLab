package com.samsung.android.plugin.tv.v3.edgeBlending.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EbDevice(
    @SerializedName("name") val name: String,
    @SerializedName("macAddress") val macAddress: String,
    @SerializedName("ip") val ip: String,
    @SerializedName("deviceId") val deviceId: String,
    val connected: Boolean,
    val available: Boolean
):Parcelable

package com.samsung.android.plugin.tv.v3.edgeBlending.datasource.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.db.models.Device

@Dao
interface DeviceDao {

    @Query(
        """
            SELECT *
            FROM devices

            ORDER BY updatedAt
        """
    )
     suspend fun getDevices(): List<Device>

    @Query(
        """
            SELECT *
            FROM devices
            WHERE id = :id

            ORDER BY updatedAt
        """
    )

   suspend fun getDevice(id:String): Device

    @Query("UPDATE devices SET connected = :connected WHERE available =:available")
    fun update(connected: Boolean?, available: Boolean?)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevice(contact: Device)
}
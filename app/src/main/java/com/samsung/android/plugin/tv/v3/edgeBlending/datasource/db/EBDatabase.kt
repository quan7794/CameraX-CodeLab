package com.samsung.android.plugin.tv.v3.edgeBlending.datasource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.db.dao.DeviceDao
import com.samsung.android.plugin.tv.v3.edgeBlending.datasource.db.models.Device

@Database(
    entities = [
        Device::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
internal abstract class EBDatabase : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao

}

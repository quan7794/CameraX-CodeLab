package com.samsung.android.plugin.tv.v3.edgeBlending.datasource.db

import androidx.room.TypeConverter
import java.util.*

internal class DateConverter {
    @TypeConverter
    fun from(date: Date?): Long? = date?.time

    @TypeConverter
    fun to(time: Long?): Date? =
        if (time != null) Date(time) else null
}
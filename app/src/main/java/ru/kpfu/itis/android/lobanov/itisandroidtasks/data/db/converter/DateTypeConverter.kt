package ru.kpfu.itis.android.lobanov.itisandroidtasks.data.db.converter

import androidx.room.TypeConverter
import java.sql.Date

class DateTypeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(value: Date?): Long? {
        return value?.time
    }
}

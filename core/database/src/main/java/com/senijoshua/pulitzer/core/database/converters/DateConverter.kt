package com.senijoshua.pulitzer.core.database.converters

import androidx.room.TypeConverter
import java.util.Date

/**
 * Type converters that converts [Date] to [Long] for persistence in the DB
 * and reverses the conversion on retrieval from the DB.
 */
class DateConverter {
    @TypeConverter
    fun timestampToDate(value: Long) = Date(value)

    @TypeConverter
    fun dateToTimestamp(date: Date) = date.time
}

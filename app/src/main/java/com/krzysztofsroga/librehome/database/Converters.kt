package com.krzysztofsroga.librehome.database

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun fromString(value: String): List<Int> {
        return value.split(",").mapNotNull { it.toIntOrNull() }
    }

    @TypeConverter
    fun fromList(list: List<Int>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }

}
package com.krzysztofsroga.librehome.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromString(value: String): List<Int> {
        return value.split(",").mapNotNull { it.toIntOrNull() }
    }

    @TypeConverter
    fun fromList(list: List<Int>): String {
        return list.joinToString(",")
    }
}
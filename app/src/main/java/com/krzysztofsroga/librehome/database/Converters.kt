package com.krzysztofsroga.librehome.database

import androidx.room.TypeConverter
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Converters {
    @TypeConverter
    fun fromString(value: String): List<Int> {
        return value.split(",").mapNotNull { it.toIntOrNull() }
    }

    @TypeConverter
    fun fromArrayList(list: List<Int>): String {
        return list.joinToString(",")
    }

//    @TypeConverter
//    fun fromString(value: String): List<Int> {
////        val listType: Type = object : TypeToken<List<Int>>() {}.type
//        return Gson().fromJson<List<Int>>(value, List::class.java)
//    }
//
//    @TypeConverter
//    fun fromArrayList(list: List<Int>): String {
//        return Gson().toJson(list)
//    }

}
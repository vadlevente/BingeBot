package com.vadlevente.bingebot.core.data.local.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject


@ProvidedTypeConverter
class DbTypeConverters @Inject constructor(
    private val gson: Gson,
) {
    @TypeConverter
    fun fromString(value: String): List<Int> {
        val listType = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromIntList(list: List<Int>): String {
        return gson.toJson(list)
    }
}
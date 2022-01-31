package com.micudasoftware.linepicker.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun toJson(dictionary: List<List<String>>): String {
        val gson = Gson()
        val type = object : TypeToken<List<List<String>>>(){}.type
        return gson.toJson(dictionary, type)
    }

    @TypeConverter
    fun fromJson(json: String) : List<List<String>> {
        val gson = Gson()
        val type = object : TypeToken<List<List<String>>>(){}.type
        return gson.fromJson(json, type)
    }
}
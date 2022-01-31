package com.micudasoftware.linepicker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Dictionary::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class DictionaryDatabase : RoomDatabase() {

    abstract fun getDictionaryDao() : DictionaryDAO
}
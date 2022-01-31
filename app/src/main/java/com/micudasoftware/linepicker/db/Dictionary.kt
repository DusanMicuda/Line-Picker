package com.micudasoftware.linepicker.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.micudasoftware.linepicker.other.Constants.DICTIONARY_TABLE_NAME

@Entity(tableName = DICTIONARY_TABLE_NAME)
data class Dictionary(
    val dictionary: List<List<String>>,
    val name: String,
    @PrimaryKey val id: Int? = null
)
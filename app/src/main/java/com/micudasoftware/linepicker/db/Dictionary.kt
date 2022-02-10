package com.micudasoftware.linepicker.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.micudasoftware.linepicker.other.Constants.DICTIONARY_TABLE_NAME

@Entity(tableName = DICTIONARY_TABLE_NAME)
data class Dictionary(
    val name: String,
    val assignment: String? = null,
    val headerColumn1: String? = null,
    val headerColumn2: String? = null,
    val headerColumn3: String? = null,
    var dictionary: List<List<String>>,
    @PrimaryKey val id: Int? = null
) {
    companion object {
        fun empty() = Dictionary(
            name = "",
            dictionary = arrayListOf(emptyList())
        )
    }
}
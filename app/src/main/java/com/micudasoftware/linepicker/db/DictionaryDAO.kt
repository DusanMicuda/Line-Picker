package com.micudasoftware.linepicker.db

import androidx.room.*
import com.micudasoftware.linepicker.other.Constants.DICTIONARY_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface DictionaryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDictionary(dictionary: Dictionary)

    @Delete
    suspend fun deleteDictionary(dictionary: Dictionary)

    @Query("SELECT * FROM $DICTIONARY_TABLE_NAME ORDER BY id")
    fun getAllDictionaries() : Flow<Dictionary>
}
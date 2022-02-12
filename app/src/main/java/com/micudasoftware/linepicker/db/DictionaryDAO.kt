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

    @Query("DELETE FROM $DICTIONARY_TABLE_NAME WHERE id = :id")
    fun deleteDictionaryById(id: Int)

    @Query("SELECT name, description, id FROM $DICTIONARY_TABLE_NAME ORDER BY id")
    fun getAllDictionaries() : Flow<List<DictionaryInfo>>

    @Query("SELECT * FROM $DICTIONARY_TABLE_NAME WHERE id = :id")
    fun getDictionaryById(id: Int) : Flow<Dictionary>
}
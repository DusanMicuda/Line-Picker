package com.micudasoftware.linepicker.repository

import android.net.Uri
import com.micudasoftware.linepicker.db.Dictionary
import com.micudasoftware.linepicker.db.DictionaryInfo
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun insertDictionary(dictionary: Dictionary)

    suspend fun deleteDictionary(dictionary: Dictionary)

    suspend fun deleteDictionaryById(id: Int)

    fun getAllDictionaries() : Flow<List<DictionaryInfo>>

    fun getDictionaryById(id: Int) : Flow<Dictionary>

    suspend fun getDictionaryFromFile(uri: Uri) : Dictionary

    suspend fun exportDictionaryToPDF(dictionary: Dictionary)
}
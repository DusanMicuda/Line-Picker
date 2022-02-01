package com.micudasoftware.linepicker.repository

import android.net.Uri
import com.micudasoftware.linepicker.db.Dictionary
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun insertDictionary(dictionary: Dictionary)

    suspend fun deleteDictionary(dictionary: Dictionary)

    fun getAllDictionaries() : Flow<Dictionary>

    suspend fun getDictionaryFromFile(uri: Uri) : Dictionary

    suspend fun exportDictionaryToPDF(dictionary: Dictionary)
}
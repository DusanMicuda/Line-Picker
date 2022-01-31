package com.micudasoftware.linepicker.repository

import com.micudasoftware.linepicker.db.Dictionary
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun insertDictionary(dictionary: Dictionary)

    suspend fun deleteDictionary(dictionary: Dictionary)

    fun getAllDictionaries() : Flow<Dictionary>
}
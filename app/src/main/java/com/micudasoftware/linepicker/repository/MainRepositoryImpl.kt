package com.micudasoftware.linepicker.repository

import com.micudasoftware.linepicker.db.Dictionary
import com.micudasoftware.linepicker.db.DictionaryDAO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val dictionaryDAO: DictionaryDAO
) : MainRepository {

    override suspend fun insertDictionary(dictionary: Dictionary) =
        dictionaryDAO.insertDictionary(dictionary)

    override suspend fun deleteDictionary(dictionary: Dictionary) =
        dictionaryDAO.deleteDictionary(dictionary)

    override fun getAllDictionaries(): Flow<Dictionary> =
        dictionaryDAO.getAllDictionaries()
}
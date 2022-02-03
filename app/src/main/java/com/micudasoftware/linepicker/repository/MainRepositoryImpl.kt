package com.micudasoftware.linepicker.repository

import android.net.Uri
import com.micudasoftware.linepicker.db.Dictionary
import com.micudasoftware.linepicker.db.DictionaryDAO
import com.micudasoftware.linepicker.fileutils.FileUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val dictionaryDAO: DictionaryDAO,
    private val fileUtils: FileUtils
) : MainRepository {

    override suspend fun insertDictionary(dictionary: Dictionary) =
        dictionaryDAO.insertDictionary(dictionary)

    override suspend fun deleteDictionary(dictionary: Dictionary) =
        dictionaryDAO.deleteDictionary(dictionary)

    override fun getAllDictionaries() =
        dictionaryDAO.getAllDictionaries()

    override suspend fun getDictionaryFromFile(uri: Uri): Dictionary =
        fileUtils.getDictionary(uri)

    override suspend fun exportDictionaryToPDF(dictionary: Dictionary) =
        fileUtils.exportToPDF(dictionary)
}
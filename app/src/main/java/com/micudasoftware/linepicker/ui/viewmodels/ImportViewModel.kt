package com.micudasoftware.linepicker.ui.viewmodels

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.micudasoftware.linepicker.repository.MainRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


class ImportViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val xls = "application/vnd.ms-excel"
    private val xlsx = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "*/*"
        putExtra(Intent.EXTRA_MIME_TYPES,  arrayOf(xlsx, xls))
        addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    }

    val dictionaries = repository.getAllDictionaries()

    fun getDictionary(uri: Uri) =
        viewModelScope.launch {
            val dictionary = repository.getDictionaryFromFile(uri)
            repository.insertDictionary(dictionary)
        }
}
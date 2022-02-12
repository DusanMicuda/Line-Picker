package com.micudasoftware.linepicker.composeui.viewmodels

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.micudasoftware.linepicker.db.DictionaryInfo
import com.micudasoftware.linepicker.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DictionaryListViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    val dictionaryList = repository.getAllDictionaries()
    var deleteLayoutIsVisible by mutableStateOf(false)
        private set
    var checkedDictionaries = mutableListOf<DictionaryInfo>()
        private set

    fun showDeleteLayout(show: Boolean) {
        if (show)
            deleteLayoutIsVisible = true
        else {
            deleteLayoutIsVisible = false
            checkedDictionaries = mutableListOf()
        }
    }

    fun getFile(uri: Uri) = viewModelScope.launch {
        val dictionary = repository.getDictionaryFromFile(uri)
        repository.insertDictionary(dictionary)
    }


    fun removeDictionaries() = viewModelScope.launch(Dispatchers.Default) {
        checkedDictionaries.forEach { dictionary ->
            repository.deleteDictionaryById(dictionary.id)
        }
        deleteLayoutIsVisible = false
        checkedDictionaries = mutableListOf()
    }
}
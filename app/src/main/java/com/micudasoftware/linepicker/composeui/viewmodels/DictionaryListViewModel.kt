package com.micudasoftware.linepicker.composeui.viewmodels

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.micudasoftware.linepicker.db.Dictionary
import com.micudasoftware.linepicker.db.DictionaryInfo
import com.micudasoftware.linepicker.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DictionaryListViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    val dictionaryList = repository.getAllDictionaries()
    var dictionary = Dictionary.empty()
        private set
    var deleteLayoutState by mutableStateOf(false)
        private set
    var checkedDictionaries = mutableListOf<DictionaryInfo>()
        private set
    var editDialogState by mutableStateOf(false)
        private set

    fun showDeleteLayout(show: Boolean) {
        if (show)
            deleteLayoutState = true
        else {
            deleteLayoutState = false
            checkedDictionaries = mutableListOf()
        }
    }

    fun getDictionaryFromFile(uri: Uri) {
        dictionary = repository.getDictionaryFromFile(uri)
        editDialogState = true
    }

    fun insertDictionary(name: String, description: String) = viewModelScope.launch {
        if (dictionary != Dictionary.empty()) {
            repository.insertDictionary(dictionary.copy(name = name, description = description))
            dictionary = Dictionary.empty()
            editDialogState = false
        }
    }

    fun editDictionary(dictionary: DictionaryInfo) {
        viewModelScope.launch {
            this@DictionaryListViewModel.dictionary = repository.getDictionaryById(dictionary.id).first()
            editDialogState = true
        }
    }

    fun cancelEdit(){
        dictionary = Dictionary.empty()
        editDialogState = false
    }

    fun removeDictionaries() = viewModelScope.launch(Dispatchers.Default) {
        checkedDictionaries.forEach { dictionary ->
            repository.deleteDictionaryById(dictionary.id)
        }
        deleteLayoutState = false
        checkedDictionaries = mutableListOf()
    }
}
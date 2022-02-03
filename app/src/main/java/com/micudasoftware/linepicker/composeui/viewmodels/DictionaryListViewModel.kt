package com.micudasoftware.linepicker.composeui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.micudasoftware.linepicker.db.Dictionary
import com.micudasoftware.linepicker.other.Event
import com.micudasoftware.linepicker.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DictionaryListViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    val dictionaryList = repository.getAllDictionaries()
    var removeLayoutIsVisible by mutableStateOf(false)
    val checkedDictionaries = mutableListOf<Dictionary>()

    fun onEvent(event: Event) {
        when(event) {
            is Event.OnGetFile -> {
                viewModelScope.launch {
                    val dictionary = repository.getDictionaryFromFile(event.uri)
                    repository.insertDictionary(dictionary)
                }
            }
            is Event.OnRemoveDictionaries -> {
                viewModelScope.launch {
                    checkedDictionaries.forEach { dictionary ->
                        repository.deleteDictionary(dictionary)
                    }
                }
            }
        }
    }
}
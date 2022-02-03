package com.micudasoftware.linepicker.composeui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.micudasoftware.linepicker.db.Dictionary
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class DictionaryViewModel : ViewModel() {

    fun randomize(dictionary: Dictionary, count: Int) : Dictionary{
        //Todo randomize
        return dictionary
    }
}

package com.micudasoftware.linepicker.composeui.viewmodels

import androidx.lifecycle.ViewModel
import com.micudasoftware.linepicker.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DictionaryListViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

}
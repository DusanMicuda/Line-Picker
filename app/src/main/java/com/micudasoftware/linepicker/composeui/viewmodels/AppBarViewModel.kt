package com.micudasoftware.linepicker.composeui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class AppBarViewModel : ViewModel() {
    var removeButtonIsVisible by mutableStateOf(false)
}
package com.micudasoftware.linepicker.ui.viewmodels


import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val preferences: SharedPreferences) : ViewModel() {

    var isFirstStart = true
    private val _canContinue = MutableLiveData(false)
    val canContinue: LiveData<Boolean>
        get() = _canContinue


    fun startTimer() {
        isFirstStart = preferences.getBoolean("isFirstStart", true)
        viewModelScope.launch {
            delay(3000)
            _canContinue.value = true
        }
    }
}
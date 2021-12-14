package com.micudasoftware.linepicker

import android.app.Application
import android.content.Context
import android.os.Handler
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SplashScreenViewModel(application: Application): AndroidViewModel(application) {

    var isFirstStart = true
    private val _canContinue = MutableLiveData<Boolean>()
    val canContinue: LiveData<Boolean>
        get() = _canContinue

    init {
        _canContinue.value = false
    }

    fun startTimer() {
        val preferences = getApplication<Application>()
            .getSharedPreferences("com.micudasoftware.linepicker", Context.MODE_PRIVATE)
        isFirstStart = preferences.getBoolean("isFirstStart", true)
        Handler().postDelayed(runnable, 3000)
    }

    private val runnable = Runnable {
        _canContinue.value = true
    }
}
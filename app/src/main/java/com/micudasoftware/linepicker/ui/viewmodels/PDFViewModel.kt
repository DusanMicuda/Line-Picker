package com.micudasoftware.linepicker.ui.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.micudasoftware.linepicker.R

class PDFViewModel(application: Application) : AndroidViewModel(application) {

    var isFirstStart = false
    val file = MutableLiveData<String>()

    private val _buttonText = MutableLiveData<String>()
    val buttonText: LiveData<String>
        get() = _buttonText

    val buttonVisible = MutableLiveData<Int>()

    private val _canContinue = MutableLiveData<Boolean>()
    val canContinue: LiveData<Boolean>
        get() = _canContinue

    init {
        _canContinue.value = false
        _buttonText.value = getApplication<Application>().getString(R.string.accept_button)
    }

    fun buttonClicked() {
        if (file.value == "LicenseAgreement.pdf") {
            val editor: SharedPreferences.Editor =
                getApplication<Application>().getSharedPreferences(
                    "com.micudasoftware.linepicker", Context.MODE_PRIVATE)
                    .edit()
            editor.putBoolean("isFirstStart", false)
            editor.apply()

            file.value = "UserManual.pdf"
            _buttonText.value = getApplication<Application>().getString(R.string.continue_button)
        } else
            _canContinue.value = true
    }
}
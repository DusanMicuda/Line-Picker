package com.micudasoftware.linepicker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.apache.poi.ss.usermodel.Row

class MainViewModel : ViewModel() {

    val assignment = MutableLiveData<String>()
    val header = MutableLiveData<Row>()
    val rows = MutableLiveData<ArrayList<Row>>()

    init {
        rows.value = ArrayList()
    }
}
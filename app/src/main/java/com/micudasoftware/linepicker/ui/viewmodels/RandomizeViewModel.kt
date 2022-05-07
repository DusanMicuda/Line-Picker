package com.micudasoftware.linepicker.ui.viewmodels

import android.view.View
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.*
import com.micudasoftware.linepicker.R
import com.micudasoftware.linepicker.db.Dictionary
import com.micudasoftware.linepicker.repository.MainRepository
import com.micudasoftware.linepicker.ui.adapters.DictionaryAdapter
import com.micudasoftware.linepicker.ui.vo.toListOfRowVo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomizeViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    val dictionary = MutableLiveData<Dictionary>()
    val adapter = DictionaryAdapter(listOf())
    var count = ""

    fun getAssignmentVisibility() =
        if (dictionary.value?.assignment.isNullOrBlank()) View.GONE else View.VISIBLE

    fun getHeaderVisibility() = if (dictionary.value?.headerColumn1.isNullOrBlank() &&
        dictionary.value?.headerColumn2.isNullOrBlank() &&
        dictionary.value?.headerColumn3.isNullOrBlank()
    ) View.GONE else View.VISIBLE

    fun loadDictionary(dictionaryId: Int) {
        viewModelScope.launch {
            repository.getDictionaryById(dictionaryId).collect { value ->
                dictionary.value = value
                updateData()
            }
        }
    }

    private fun updateData() {
        dictionary.value?.let {
            adapter.updateData(it.toListOfRowVo())
        }
    }

    fun randomize(view: View) {
        if (count.isNotBlank() && count.isDigitsOnly()) {
            if (count.toInt() > 0 && count.toInt() <= dictionary.value?.dictionary?.size ?: 0) {
                val randomized = dictionary.value?.dictionary?.shuffled()?.subList(0, count.toInt())
                dictionary.value?.apply {
                    if (randomized != null) {
                        this.dictionary = randomized
                        updateData()
                    }
                }
                //TODO navigate to export fragment
            } else {
                Toast.makeText(view.context, R.string.randomize_error_count_range, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(view.context, R.string.randomize_error_count_empty, Toast.LENGTH_SHORT).show()
        }
    }
}

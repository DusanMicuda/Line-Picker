package com.micudasoftware.linepicker.ui.viewmodels

import android.view.View
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.findFragment
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.micudasoftware.linepicker.R
import com.micudasoftware.linepicker.db.Dictionary
import com.micudasoftware.linepicker.ui.viewstates.DictionaryFragmentState
import com.micudasoftware.linepicker.repository.MainRepository
import com.micudasoftware.linepicker.ui.adapters.DictionaryAdapter
import com.micudasoftware.linepicker.ui.fragments.RandomizeFragmentDirections
import com.micudasoftware.linepicker.ui.vo.toListOfRowVo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomizeViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _dictionary = MutableLiveData<Dictionary>()
    val dictionary: LiveData<Dictionary> = _dictionary

    val adapter = DictionaryAdapter(listOf()) //TODO try to observe dictionary in adapter
    val count = MutableLiveData<String?>()

    private val _randomizeEnabled = MutableLiveData(false)
    val randomizeEnabled: LiveData<Boolean> = _randomizeEnabled

    private val _countErrorText = MutableLiveData<Int>()
    val countErrorText: LiveData<Int> = _countErrorText

    private val _fragmentState =
        MutableLiveData<DictionaryFragmentState>(DictionaryFragmentState.Loading)
    val fragmentState: LiveData<DictionaryFragmentState> = _fragmentState

    fun getAssignmentVisibility() =
        if (dictionary.value?.assignment.isNullOrBlank()) View.GONE else View.VISIBLE

    fun getHeaderVisibility() = if (dictionary.value?.headerColumn1.isNullOrBlank() &&
        dictionary.value?.headerColumn2.isNullOrBlank() &&
        dictionary.value?.headerColumn3.isNullOrBlank()
    ) View.GONE else View.VISIBLE

    fun loadDictionary(dictionaryId: Int) {
        viewModelScope.launch {
            repository.getDictionaryById(dictionaryId).collect { value ->
                _dictionary.value = value
                _fragmentState.value = DictionaryFragmentState.Loaded
            }
        }
    }

    fun updateData() {
        dictionary.value?.let { adapter.updateData(it.toListOfRowVo()) }
    }

    fun onRandomizeButtonClick(view: View) {
        view.findNavController()
            .navigate(RandomizeFragmentDirections.actionRandomizeFragmentToRandomizeDialog())
    }

    fun randomize() {
        val randomized = count.value?.let { dictionary.value?.dictionary?.shuffled()?.subList(0, it.toInt()) }
        dictionary.value?.apply {
            if (randomized != null) {
                this.dictionary = randomized
            }
        }
        count.value = null
        _fragmentState.value = DictionaryFragmentState.Randomized
    }

    fun validateCount(count: String) {
        _countErrorText.value = when {
            count.isBlank() || !count.isDigitsOnly() -> {
                _randomizeEnabled.value = false
                R.string.randomize_error_count_empty
            }
            count.toInt() <= 0 || count.toInt() > (dictionary.value?.dictionary?.size!!) -> {
                _randomizeEnabled.value = false
                R.string.randomize_error_count_range
            }
            else -> {
                _randomizeEnabled.value = true
                null
            }
        }
    }
}

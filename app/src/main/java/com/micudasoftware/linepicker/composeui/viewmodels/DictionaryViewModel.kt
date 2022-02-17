package com.micudasoftware.linepicker.composeui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.micudasoftware.linepicker.db.Dictionary
import com.micudasoftware.linepicker.db.DictionaryInfo
import com.micudasoftware.linepicker.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.last
import java.util.concurrent.Flow
import javax.inject.Inject

@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    lateinit var dictionary: State<Dictionary>
    var randomizeDialogState by mutableStateOf(false)
        private set

    fun showRandomizeDialog() {
        randomizeDialogState = true
    }

    fun cancelRandomize() {
        randomizeDialogState = false
    }
    fun getDictionary(id: Int) = repository.getDictionaryById(id)

    fun randomize(count: Int) {
        val randomized = dictionary.value.dictionary.shuffled().subList(0, count)
        dictionary.value.dictionary = randomized
    }
}

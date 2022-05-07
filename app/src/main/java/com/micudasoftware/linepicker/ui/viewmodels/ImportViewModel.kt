package com.micudasoftware.linepicker.ui.viewmodels

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.micudasoftware.linepicker.db.DictionaryInfo
import com.micudasoftware.linepicker.repository.MainRepository
import com.micudasoftware.linepicker.ui.adapters.DictionaryListAdapter
import com.micudasoftware.linepicker.ui.adapters.DictionaryListItemListener
import com.micudasoftware.linepicker.ui.fragments.ImportFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImportViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel(), DictionaryListItemListener {

    private val xls = "application/vnd.ms-excel"
    private val xlsx = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "*/*"
        putExtra(Intent.EXTRA_MIME_TYPES,  arrayOf(xlsx, xls))
        addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    }

    val dictionaries = repository.getAllDictionaries()

    val adapter = DictionaryListAdapter(listOf(), this)

    init {
        viewModelScope.launch {
            dictionaries.collect {
                adapter.updateData(it)
            }
        }
    }

    fun getDictionary(uri: Uri) {
        val dictionary = repository.getDictionaryFromFile(uri)
        viewModelScope.launch {
            repository.insertDictionary(dictionary)
        }
    }

    override fun onDictionaryListItemClick(view: View, item: DictionaryInfo) {
        view.findNavController().navigate(
            ImportFragmentDirections.actionImportFragmentToRandomizeFragment(item.id)
        )
    }

}
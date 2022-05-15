package com.micudasoftware.linepicker.ui.viewstates

sealed class DictionaryFragmentState {
    object Loading : DictionaryFragmentState()
    object Loaded : DictionaryFragmentState()
    object Randomized : DictionaryFragmentState()
}

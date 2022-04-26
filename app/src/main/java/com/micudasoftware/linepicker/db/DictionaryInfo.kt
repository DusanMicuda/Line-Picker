package com.micudasoftware.linepicker.db

import com.micudasoftware.linepicker.ui.adapters.ListAdapterItem

data class DictionaryInfo(
    val name: String,
    val description: String?,
    override val id: Int
) : ListAdapterItem

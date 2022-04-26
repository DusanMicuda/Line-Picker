package com.micudasoftware.linepicker.ui.adapters

import android.view.View
import com.micudasoftware.linepicker.R
import com.micudasoftware.linepicker.databinding.ItemDictionaryBinding
import com.micudasoftware.linepicker.db.DictionaryInfo

class DictionaryListAdapter(
    list: List<DictionaryInfo>,
    private val listener: DictionaryListItemListener
) : BaseAdapter<ItemDictionaryBinding, DictionaryInfo>(list) {

    override val layoutId: Int = R.layout.item_dictionary

    override fun bind(binding: ItemDictionaryBinding, item: DictionaryInfo) {
        binding.apply {
            dictionary = item
            listener = this@DictionaryListAdapter.listener
            executePendingBindings()
        }
    }
}

interface DictionaryListItemListener {
    fun onDictionaryListItemClick(view: View, item: DictionaryInfo)
}
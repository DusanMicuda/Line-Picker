package com.micudasoftware.linepicker.ui.adapters

import com.micudasoftware.linepicker.R
import com.micudasoftware.linepicker.databinding.ItemRowBinding
import com.micudasoftware.linepicker.ui.vo.RowVo

class DictionaryAdapter(
    list: List<RowVo>
) : BaseAdapter<ItemRowBinding, RowVo>(list) {

    override val layoutId: Int = R.layout.item_row

    override fun bind(binding: ItemRowBinding, item: RowVo) {
        binding.apply {
            row = item
            executePendingBindings()
        }
    }
}

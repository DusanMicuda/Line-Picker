package com.micudasoftware.linepicker.ui.vo

import com.micudasoftware.linepicker.db.Dictionary
import com.micudasoftware.linepicker.ui.adapters.ListAdapterItem

data class RowVo(
    override val id: Int,
    val column1: String = "",
    val column2: String = "",
    val column3: String = "",
) : ListAdapterItem

fun Dictionary.toListOfRowVo() : List<RowVo>{
    val rows = ArrayList<RowVo>()
    dictionary.forEachIndexed { index, list ->
        if (list.isNotEmpty()) {
            rows.add(
                RowVo(index, list[0], list[1], list[2])
            )
        }
    }
    return rows
}
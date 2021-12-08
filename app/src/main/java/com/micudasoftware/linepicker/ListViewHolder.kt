package com.micudasoftware.linepicker

import android.content.res.Resources
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.apache.poi.ss.usermodel.Row
import java.lang.StringBuilder

class ListViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val columnWord: TextView = view.findViewById(R.id.column_word)
    private val columnPronunciation: TextView = view.findViewById(R.id.column_pronunciation)
    private val columnTranslation: TextView = view.findViewById(R.id.column_translation)


    fun setRowData(row: Row) {
        val stringBuilder = StringBuilder()
        val iterator = row.cellIterator()
        while (iterator.hasNext()) {

            val cell = iterator.next()
            when (cell.columnIndex) {
                1 -> columnWord.text = cell.stringCellValue
                2 -> columnPronunciation.text = cell.stringCellValue
                in 3..12 -> {
                    if (cell.stringCellValue != "") {
                        if (stringBuilder.toString() != "")
                            stringBuilder.append("\n")
                        stringBuilder.append(cell.stringCellValue)
                    }
                }

            }
        }
        columnTranslation.text = stringBuilder.toString()
    }
}
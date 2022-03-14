package com.micudasoftware.linepicker.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.micudasoftware.linepicker.R
import com.micudasoftware.linepicker.db.Dictionary
import kotlinx.coroutines.flow.Flow

class DictionaryListAdapter(private val dictionaries: Flow<List<Dictionary>>): RecyclerView.Adapter<ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.setRowData(rows[position])
    }

    override fun getItemCount(): Int {
        return rows.size
    }
}
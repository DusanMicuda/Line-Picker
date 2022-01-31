package com.micudasoftware.linepicker.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.micudasoftware.linepicker.R
import org.apache.poi.ss.usermodel.Row

class ListAdapter(private val rows: ArrayList<Row>): RecyclerView.Adapter<ListViewHolder>() {

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
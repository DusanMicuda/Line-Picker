package com.micudasoftware.linepicker

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.apache.poi.ss.usermodel.Row

class ListAdapter(private val rows: ArrayList<Row>): RecyclerView.Adapter<ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        Log.d("debugujem", "ListAdapter onCreateViewHolder")

        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val row = rows[position]
        holder.setRowData(row)
        Log.d("debugujem", "ListAdapter onBindViewHolder")
    }

    override fun getItemCount(): Int {
        return rows.size
    }
}
package com.micudasoftware.linepicker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.micudasoftware.linepicker.databinding.FragmentRandomizeBinding

class RandomizeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentRandomizeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_randomize, container, false)
        val viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        if (viewModel.assignment.value != null) {
            binding.assignment.text = viewModel.assignment.value
            binding.assignment.visibility = View.VISIBLE
        }

        val cellIterator = viewModel.header.value?.cellIterator()
        if (cellIterator != null) {
            var column1 = ""
            var column2 = ""
            var column3 = ""
            while (cellIterator.hasNext()) {
                val cell = cellIterator.next()
                when (cell.columnIndex) {
                    1 -> column1 = cell.stringCellValue
                    2 -> column2 = cell.stringCellValue
                    3 -> column3 = cell.stringCellValue
                }
            }
            if (column1 != "" && column2 != "" && column3 != "") {
                binding.column1.text = column1
                binding.column2.text = column2
                binding.column3.text = column3
                binding.header.visibility = View.VISIBLE
            }

        }

        binding.randomizeList.layoutManager = LinearLayoutManager(activity)
        binding.randomizeList.adapter = viewModel.rows.value?.let { ListAdapter(it) }

        return binding.root
    }
}
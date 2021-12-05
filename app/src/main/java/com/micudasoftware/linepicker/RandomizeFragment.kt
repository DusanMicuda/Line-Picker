package com.micudasoftware.linepicker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.micudasoftware.linepicker.databinding.FragmentImportBinding
import com.micudasoftware.linepicker.databinding.FragmentRandomizeBinding

class RandomizeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentRandomizeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_randomize, container, false)
        val viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding.randomizeList.layoutManager = LinearLayoutManager(activity)
        binding.randomizeList.adapter = ListAdapter(viewModel.rows.value!!)
        Log.d("debugujem", "randomize onCreateView")
//        Log.d("debugujem", viewModel.rows.value!!.get(0).cellIterator().next().stringCellValue)

        return binding.root
    }
}
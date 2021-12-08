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
import androidx.lifecycle.Observer
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
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.randomizeList.layoutManager = LinearLayoutManager(activity)

        viewModel.rows.observe(viewLifecycleOwner, { binding.randomizeList.adapter = ListAdapter(it) })

        return binding.root
    }
}
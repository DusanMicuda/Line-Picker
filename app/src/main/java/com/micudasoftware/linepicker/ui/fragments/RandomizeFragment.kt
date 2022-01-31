package com.micudasoftware.linepicker.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.micudasoftware.linepicker.ui.adapters.ListAdapter
import com.micudasoftware.linepicker.ui.viewmodels.MainViewModel
import com.micudasoftware.linepicker.R
import com.micudasoftware.linepicker.databinding.FragmentRandomizeBinding

class RandomizeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentRandomizeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_randomize, container, false)
        val viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.randomizeList.layoutManager = LinearLayoutManager(activity)

        viewModel.rows.observe(viewLifecycleOwner) {
            binding.randomizeList.adapter = ListAdapter(it)
        }

        binding.randomize.setOnClickListener {
                if (!binding.count.text.isNullOrBlank() &&
                    binding.count.text.isDigitsOnly() &&
                    viewModel.randomize(binding.count.text.toString().toInt()))
                        view?.findNavController()?.navigate(R.id.action_randomizeFragment_to_exportFragment)
                else
                    Toast.makeText(requireContext(), "Count isn`t in range!", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }
}
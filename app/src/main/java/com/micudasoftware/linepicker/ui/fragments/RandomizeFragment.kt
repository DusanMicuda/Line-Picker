package com.micudasoftware.linepicker.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.micudasoftware.linepicker.R
import com.micudasoftware.linepicker.databinding.FragmentRandomizeBinding
import com.micudasoftware.linepicker.ui.viewstates.DictionaryFragmentState
import com.micudasoftware.linepicker.ui.viewmodels.RandomizeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomizeFragment : Fragment() {

    private val args by navArgs<RandomizeFragmentArgs>()
    private val viewModel: RandomizeViewModel by activityViewModels()
    private lateinit var binding: FragmentRandomizeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<FragmentRandomizeBinding?>(
            inflater,
            R.layout.fragment_randomize,
            container,
            false
        ).apply {
            viewModel = this@RandomizeFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        registerEvents()

        return binding.root
    }

    private fun registerEvents() {
        viewModel.loadDictionary(args.dictionaryId)

        viewModel.fragmentState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is DictionaryFragmentState.Loaded -> viewModel.updateData()
                is DictionaryFragmentState.Randomized -> viewModel.updateData()
            }
        }
    }
}
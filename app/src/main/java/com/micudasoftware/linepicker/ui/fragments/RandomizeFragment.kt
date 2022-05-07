package com.micudasoftware.linepicker.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.micudasoftware.linepicker.R
import com.micudasoftware.linepicker.databinding.FragmentRandomizeBinding
import com.micudasoftware.linepicker.ui.viewmodels.RandomizeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomizeFragment : Fragment() {

    private val args by navArgs<RandomizeFragmentArgs>()
    private val viewModel: RandomizeViewModel by viewModels()
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

        viewModel.loadDictionary(args.dictionaryId)

        return binding.root
    }
}
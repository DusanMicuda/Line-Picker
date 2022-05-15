package com.micudasoftware.linepicker.ui.fragments

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.micudasoftware.linepicker.R
import com.micudasoftware.linepicker.databinding.DialogRandomizeBinding
import com.micudasoftware.linepicker.ui.viewmodels.RandomizeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomizeDialog : DialogFragment() {

    private val viewModel: RandomizeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<DialogRandomizeBinding>(
            layoutInflater,
            R.layout.dialog_randomize,
            container,
            false
        )

        binding.viewModel = viewModel
        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
        binding.buttonRandomize.setOnClickListener {
            viewModel.randomize()
            dismiss()
        }
        viewModel.countErrorText.observe(viewLifecycleOwner) { errorTextRes ->
            val errorText = errorTextRes?.let { getString(it) }
            binding.textInputLayoutCount.error = errorText
        }
        viewModel.count.observe(viewLifecycleOwner) { count ->
            if (count != null) {
                viewModel.validateCount(count)
            }
        }
        viewModel.randomizeEnabled.observe(viewLifecycleOwner) {
            binding.buttonRandomize.isEnabled = it
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}

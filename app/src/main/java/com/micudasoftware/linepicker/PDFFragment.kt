package com.micudasoftware.linepicker

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.micudasoftware.linepicker.databinding.FragmentPdfBinding

class PDFFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.show()
        activity?.window?.statusBarColor = Color.parseColor("#3da0d7")

        val binding: FragmentPdfBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_pdf, container, false)

        val viewModel = ViewModelProvider(this)[PDFViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.buttonVisible.value =
            if (PDFFragmentArgs.fromBundle(requireArguments()).isFirstStart)
                View.VISIBLE
            else
                View.INVISIBLE
        viewModel.file.value = PDFFragmentArgs.fromBundle(requireArguments()).file

        viewModel.file.observe(viewLifecycleOwner, Observer { binding.PDFView.fromAsset(it).load() })
        viewModel.canContinue.observe(viewLifecycleOwner, Observer {
            if (it)
                view?.findNavController()?.navigate(R.id.action_PDFFragment_to_importFragment)
        })

        return binding.root
    }
}
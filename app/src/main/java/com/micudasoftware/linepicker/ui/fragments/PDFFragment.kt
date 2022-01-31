package com.micudasoftware.linepicker.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.micudasoftware.linepicker.ui.viewmodels.PDFViewModel
import com.micudasoftware.linepicker.R
import com.micudasoftware.linepicker.databinding.FragmentPdfBinding

class PDFFragment : Fragment() {

    private lateinit var viewModel: PDFViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.show()
        activity?.window?.statusBarColor = resources.getColor(R.color.colorPrimaryVariant)

        val binding: FragmentPdfBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_pdf, container, false)

        viewModel = ViewModelProvider(this)[PDFViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.isFirstStart = PDFFragmentArgs.fromBundle(requireArguments()).isFirstStart
        viewModel.file.value = PDFFragmentArgs.fromBundle(requireArguments()).file
        viewModel.buttonVisible.value =
            if (viewModel.isFirstStart)
                View.VISIBLE
            else
                View.INVISIBLE

        viewModel.file.observe(viewLifecycleOwner) { binding.PDFView.fromAsset(it).load() }
        viewModel.canContinue.observe(viewLifecycleOwner) {
            if (it)
                view?.findNavController()?.navigate(R.id.action_PDFFragment_to_importFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.isFirstStart)
            setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }
}
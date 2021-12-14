package com.micudasoftware.linepicker

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.micudasoftware.linepicker.databinding.FragmentPdfBinding

class PDFFragment : Fragment() {

    private lateinit var viewModel: PDFViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.show()
        activity?.window?.statusBarColor = Color.parseColor("#3da0d7")

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

        viewModel.file.observe(viewLifecycleOwner, Observer { binding.PDFView.fromAsset(it).load() })
        viewModel.canContinue.observe(viewLifecycleOwner, Observer {
            if (it)
                view?.findNavController()?.navigate(R.id.action_PDFFragment_to_importFragment)
        })

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
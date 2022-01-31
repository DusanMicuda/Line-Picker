package com.micudasoftware.linepicker.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.micudasoftware.linepicker.ui.adapters.ListAdapter
import com.micudasoftware.linepicker.ui.viewmodels.MainViewModel
import com.micudasoftware.linepicker.R
import com.micudasoftware.linepicker.databinding.FragmentExportBinding

class ExportFragment : Fragment() {

    lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentExportBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_export, container, false)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        binding.viewModel = viewModel
        binding.exportFragment = this
        binding.lifecycleOwner = viewLifecycleOwner

        binding.randomizeList.layoutManager = LinearLayoutManager(activity)
        viewModel.randomizedRows.observe(viewLifecycleOwner) {
            binding.randomizeList.adapter = ListAdapter(it)
        }

        return binding.root
    }

    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED) {
            viewModel.exportToPDF()
        } else
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted)
                viewModel.exportToPDF()
            else
                Toast.makeText(requireContext(), "Access Denied!", Toast.LENGTH_SHORT).show()
        }
}
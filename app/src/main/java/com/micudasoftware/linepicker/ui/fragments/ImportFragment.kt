package com.micudasoftware.linepicker.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.micudasoftware.linepicker.ui.viewmodels.MainViewModel
import com.micudasoftware.linepicker.R
import com.micudasoftware.linepicker.databinding.FragmentImportBinding

class ImportFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.show()
        activity?.window?.statusBarColor = resources.getColor(R.color.colorPrimaryVariant)

        val binding: FragmentImportBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_import, container, false)

        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.importButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
                    resultLauncher.launch(viewModel.intent)
            } else
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        return binding.root
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { data ->
                viewModel.readExcelData(data)
                view?.findNavController()?.navigate(R.id.action_importFragment_to_randomizeFragment)
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted)
                resultLauncher.launch(viewModel.intent)
            else
                Toast.makeText(requireContext(), "Access Denied!", Toast.LENGTH_SHORT).show()
        }

}
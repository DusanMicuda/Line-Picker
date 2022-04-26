package com.micudasoftware.linepicker.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.micudasoftware.linepicker.ui.viewmodels.MainViewModel
import com.micudasoftware.linepicker.R
import com.micudasoftware.linepicker.databinding.FragmentImportBinding
import com.micudasoftware.linepicker.ui.viewmodels.ImportViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImportFragment : Fragment() {

    private val viewModel: ImportViewModel by viewModels()
    private lateinit var binding: FragmentImportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.show()
        activity?.window?.statusBarColor = resources.getColor(R.color.colorPrimaryVariant)

        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_import, container, false)

        binding.apply {
            viewModel = viewModel
            fragment = this@ImportFragment
            lifecycleOwner = viewLifecycleOwner
            dictionaryList.layoutManager = LinearLayoutManager(context)
            dictionaryList.adapter = this@ImportFragment.viewModel.adapter
        }

        return binding.root
    }

    fun importFile() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED) {
            resultLauncher.launch(viewModel.intent)
        } else
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { data ->
                viewModel.getDictionary(data)
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
package com.micudasoftware.linepicker

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.micudasoftware.linepicker.databinding.FragmentExportBinding
import org.apache.poi.ss.usermodel.Row
import java.io.FileOutputStream
import java.io.IOException
import java.lang.StringBuilder
import java.util.ArrayList

class ExportFragment : Fragment() {

    lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentExportBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_export, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.exportFragment = this
        binding.lifecycleOwner = viewLifecycleOwner

        binding.randomizeList.layoutManager = LinearLayoutManager(activity)
        viewModel.randomizedRows.observe(viewLifecycleOwner, { binding.randomizeList.adapter = ListAdapter(it) })

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
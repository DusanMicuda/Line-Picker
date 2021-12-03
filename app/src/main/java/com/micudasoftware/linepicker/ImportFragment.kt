package com.micudasoftware.linepicker

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.micudasoftware.linepicker.databinding.FragmentImportBinding
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class ImportFragment : Fragment() {


    private val xls = "application/vnd.ms-excel"
    private val xlsx = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentImportBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_import, container, false)
        binding.handler = this
        return binding.root
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { data ->
                readExcelData(data)
                view?.let { Navigation.findNavController(it).navigate(R.id.action_importFragment_to_randomizeFragment) }
            }
        }
    }

    fun importFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        val mimetypes = arrayOf(xlsx, xls)
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)

        resultLauncher.launch(intent)
    }

    /*
    3 stlpce, preklady rozdelit enterom, v subore 10 stlpcov, prve dva + 8 preklaadov
    prvy riadok zadanie, druhy hlavicka
     */

    private fun readExcelData(excelFile: Uri) {
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val inputStream = context?.contentResolver?.openInputStream(excelFile)

        val workbook: Workbook =
            when (context?.contentResolver?.getType(excelFile)) {
                xls -> HSSFWorkbook(inputStream)
                xlsx -> XSSFWorkbook(inputStream)
                else -> return
            }

        val sheet = workbook.getSheetAt(0)
        for (row: Row in sheet) {
            when (row.rowNum) {
                0 -> if (row.cellIterator().hasNext())
                    viewModel.assignment.value = row.cellIterator().next().stringCellValue
                1 -> viewModel.header.value = row
                else -> viewModel.rows.value?.add(row)
            }
        }
    }
}
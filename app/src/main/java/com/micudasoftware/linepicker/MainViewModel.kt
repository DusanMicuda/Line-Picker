package com.micudasoftware.linepicker

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val xls = "application/vnd.ms-excel"
    private val xlsx = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    val startResultLauncher = MutableLiveData<Boolean>()
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
    val assignment = MutableLiveData<String>()
    val headerColumn1 = MutableLiveData<String>()
    val headerColumn2 = MutableLiveData<String>()
    val headerColumn3 = MutableLiveData<String>()
    val rows = MutableLiveData<ArrayList<Row>>()

    init {
        startResultLauncher.value = false

        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES,  arrayOf(xlsx, xls))
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)

        rows.value = ArrayList()
    }

    fun importFile() {
        startResultLauncher.value = true
    }

    fun getAssignmentVisibility() = if (assignment.value.isNullOrBlank()) View.GONE else View.VISIBLE

    fun getHeaderVisibility() = if (headerColumn1.value.isNullOrBlank() &&
                                    headerColumn2.value.isNullOrBlank() &&
                                    headerColumn3.value.isNullOrBlank())
                                        View.GONE else View.VISIBLE

    fun readExcelData(excelFile: Uri) {
        startResultLauncher.value = false
        val inputStream = getApplication<Application>().contentResolver?.openInputStream(excelFile)

        val workbook: Workbook =
            when (getApplication<Application>().contentResolver?.getType(excelFile)) {
                xls -> HSSFWorkbook(inputStream)
                xlsx -> XSSFWorkbook(inputStream)
                else -> return
            }

        assignment.value = null
        headerColumn1.value = null
        headerColumn2.value = null
        headerColumn3.value = null
        val sheet = workbook.getSheetAt(0)
        for (row: Row in sheet) {
            when (row.rowNum) {
                0 -> if (row.cellIterator().hasNext())
                    assignment.value = row.cellIterator().next().stringCellValue
                1 -> {
                    val cellIterator = row.cellIterator()
                    if (cellIterator != null) {
                        while (cellIterator.hasNext()) {
                            val cell = cellIterator.next()
                            when (cell.columnIndex) {
                                1 -> headerColumn1.value = cell.stringCellValue
                                2 -> headerColumn2.value = cell.stringCellValue
                                3 -> headerColumn3.value = cell.stringCellValue
                            }
                        }
                    }
                }
                else -> rows.value?.add(row)
            }
        }
    }
}
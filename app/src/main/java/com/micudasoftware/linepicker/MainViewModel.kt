package com.micudasoftware.linepicker

import android.app.Application
import android.app.Notification
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream
import java.io.IOException
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val xls = "application/vnd.ms-excel"
    private val xlsx = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)

    private val _assignment = MutableLiveData<String>()
    val assignment: LiveData<String>
        get() = _assignment

    private val _headerColumn1 = MutableLiveData<String>()
    val headerColumn1: LiveData<String>
        get() = _headerColumn1

    private val _headerColumn2 = MutableLiveData<String>()
    val headerColumn2: LiveData<String>
        get() = _headerColumn2

    private val _headerColumn3 = MutableLiveData<String>()
    val headerColumn3: LiveData<String>
        get() = _headerColumn3

    private val _hint = MutableLiveData<String>()
    val hint: LiveData<String>
        get() = _hint

    private val _rows = MutableLiveData<ArrayList<Row>>()
    val rows: LiveData<ArrayList<Row>>
        get() = _rows

    private val _randomizedRows = MutableLiveData<ArrayList<Row>>()
    val randomizedRows: LiveData<ArrayList<Row>>
        get() = _randomizedRows

    init {
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES,  arrayOf(xlsx, xls))
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)

        _rows.value = ArrayList()
    }

    fun getAssignmentVisibility() = if (_assignment.value.isNullOrBlank()) View.GONE else View.VISIBLE

    fun getHeaderVisibility() = if (_headerColumn1.value.isNullOrBlank() &&
                                    _headerColumn2.value.isNullOrBlank() &&
                                    _headerColumn3.value.isNullOrBlank())
                                        View.GONE else View.VISIBLE

    fun readExcelData(excelFile: Uri) {
        _rows.value = ArrayList()
        val inputStream = getApplication<Application>().contentResolver?.openInputStream(excelFile)

        val workbook: Workbook =
            when (getApplication<Application>().contentResolver?.getType(excelFile)) {
                xls -> HSSFWorkbook(inputStream)
                xlsx -> XSSFWorkbook(inputStream)
                else -> return
            }

        _assignment.value = null
        _headerColumn1.value = null
        _headerColumn2.value = null
        _headerColumn3.value = null
        val sheet = workbook.getSheetAt(0)
        for (row: Row in sheet) {
            when (row.rowNum) {
                0 -> if (row.cellIterator().hasNext())
                    _assignment.value = row.cellIterator().next().stringCellValue
                1 -> {
                    val cellIterator = row.cellIterator()
                    if (cellIterator != null) {
                        while (cellIterator.hasNext()) {
                            val cell = cellIterator.next()
                            when (cell.columnIndex) {
                                1 -> _headerColumn1.value = cell.stringCellValue
                                2 -> _headerColumn2.value = cell.stringCellValue
                                3 -> _headerColumn3.value = cell.stringCellValue
                            }
                        }
                    }
                }
                else -> _rows.value?.add(row)
            }
        }
        _hint.value = getApplication<Application>().getString(R.string.hint, _rows.value?.size)
    }

    fun randomize(count: Int): Boolean{
        val cells = ArrayList(_rows.value)
        _randomizedRows.value = ArrayList()

        if (count < 1 || count > cells.size)
            return false

        for (i in 0 until count)
        {
            val rand = Random()
            val index = rand.nextInt(cells.size)
            _randomizedRows.value?.add(cells[index])
            cells.removeAt(index)
        }
        return true
    }

    fun exportToPDF() {
        val pdfDocument = PdfDocument()

        val textPaint = TextPaint(Color.BLACK)
        textPaint.textSize = 14f

        val assignmentPaint = TextPaint(Color.BLACK)
        assignmentPaint.textSize = 16f
        assignmentPaint.typeface = Typeface.DEFAULT_BOLD

        val pageHeight = 842
        val pageWidth = 595
        var pageNumber = 1

        var myPage = pdfDocument.startPage(
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        )

        var canvas = myPage.canvas
        canvas.drawBitmap(
            BitmapFactory.decodeResource(getApplication<Application>().resources, R.drawable.logo),
            null,
            RectF(247F, 770F, 347F, 820F),
            null)

        var height = 50
        canvas.translate(50f, 50f)

        if (getAssignmentVisibility() == View.VISIBLE) {
            val staticLayout = StaticLayout(
                _assignment.value, assignmentPaint, pageWidth - 100,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
            staticLayout.draw(canvas)
            canvas.translate(0f, (staticLayout.height + 30).toFloat())
            height += staticLayout.height + 30
        }

        if (getHeaderVisibility() == View.VISIBLE) {
            var staticLayout = StaticLayout(
                _headerColumn1.value, assignmentPaint, (pageWidth-150)/3,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
            staticLayout.draw(canvas)

            canvas.translate((pageWidth-150)/3 + 25F, 0F)
            staticLayout = StaticLayout(
                _headerColumn2.value, assignmentPaint, (pageWidth-150)/3,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
            staticLayout.draw(canvas)

            canvas.translate((pageWidth-150)/3 + 25F, 0F)
            staticLayout = StaticLayout(
                _headerColumn3.value, assignmentPaint, (pageWidth-150)/3,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
            staticLayout.draw(canvas)

            canvas.translate(-(((pageWidth-150)/3 + 25F)*2), (staticLayout.height + 30).toFloat())
            height += staticLayout.height + 30
        }

        for (row in _randomizedRows.value!!) {
            var column1 = ""
            var column2 = ""
            val column3 = StringBuilder()
            val iterator = row.cellIterator()
            while (iterator.hasNext()) {
                val cell = iterator.next()
                when (cell.columnIndex) {
                    1 -> column1 = cell.stringCellValue
                    2 -> column2 = cell.stringCellValue
                    in 3..12 -> {
                        if (cell.stringCellValue != "") {
                            if (column3.toString() != "")
                                column3.append("\n")
                            column3.append(cell.stringCellValue)
                        }
                    }
                }
            }

            val column1Layout = StaticLayout(
                column1, textPaint, (pageWidth-150)/3,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
            val column2Layout = StaticLayout(
                column2, textPaint, (pageWidth-150)/3,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
            val column3Layout = StaticLayout(
                column3.toString(), textPaint, (pageWidth-150)/3,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)

            val layoutHeight = column1Layout.height.coerceAtLeast(
                column2Layout.height.coerceAtLeast(column3Layout.height))

            if (height + layoutHeight + 100 < pageHeight) {
                column1Layout.draw(canvas)
                canvas.translate((pageWidth-150)/3 + 25F, 0F)
                column2Layout.draw(canvas)
                canvas.translate((pageWidth-150)/3 + 25F, 0F)
                column3Layout.draw(canvas)

                canvas.translate(-(((pageWidth-150)/3 + 25F)*2), (layoutHeight + 20).toFloat())
                height += layoutHeight + 20
            } else {
                pdfDocument.finishPage(myPage)
                pageNumber += 1
                myPage = pdfDocument.startPage(
                    PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
                )
                canvas = myPage.canvas
                canvas.drawBitmap(
                    BitmapFactory.decodeResource(
                        getApplication<Application>().resources,
                        R.drawable.logo
                    ), null,
                    RectF(247F, 770F, 347F, 820F), null
                )
                canvas.translate(50f, 50f)
                height = 50

                column1Layout.draw(canvas)
                canvas.translate((pageWidth-150)/3 + 25F, 0F)
                column2Layout.draw(canvas)
                canvas.translate((pageWidth-150)/3 + 25F, 0F)
                column3Layout.draw(canvas)

                canvas.translate(-(((pageWidth-150)/3 + 25F)*2), (layoutHeight + 20).toFloat())
                height += layoutHeight + 20
            }
        }
        pdfDocument.finishPage(myPage)

        val displayName = "LinePicker-" + System.currentTimeMillis() + ".pdf"
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            contentValues.put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOCUMENTS)
        else contentValues.put(
            MediaStore.MediaColumns.DATA,
            Environment.getExternalStorageDirectory().toString() + "/" + displayName)

        val resolver: ContentResolver = getApplication<Application>().contentResolver
        val uri: Uri?
        try {
            uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
            val pfd: ParcelFileDescriptor? = uri?.let { resolver.openFileDescriptor(it, "w") }
            pdfDocument.writeTo(FileOutputStream(pfd?.fileDescriptor))
            pfd?.close()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/pdf")
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val pendingIntent = PendingIntent.getActivity(getApplication(), 0, intent, 0)
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(getApplication(), "1")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("PDF file generated successfully")
                .setContentText("Tap to open")
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
            val notificationManager = NotificationManagerCompat.from(getApplication())
            notificationManager.notify(1, builder.build())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        pdfDocument.close()
    }
}
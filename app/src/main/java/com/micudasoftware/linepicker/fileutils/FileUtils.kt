package com.micudasoftware.linepicker.fileutils

import android.app.Notification
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
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
import android.provider.OpenableColumns
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.micudasoftware.linepicker.R
import com.micudasoftware.linepicker.db.Dictionary
import com.micudasoftware.linepicker.other.Constants.FILE_TYPE_XLS
import com.micudasoftware.linepicker.other.Constants.FILE_TYPE_XLSX
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream
import java.io.IOException


class FileUtils(private val context: Context) {

    fun getDictionary(excelFile: Uri) : Dictionary{
        val dictionary = ArrayList<ArrayList<String>>()
        val inputStream = context.contentResolver?.openInputStream(excelFile)

        val workbook: Workbook =
            when (context.contentResolver?.getType(excelFile)) {
                FILE_TYPE_XLS -> HSSFWorkbook(inputStream)
                FILE_TYPE_XLSX -> XSSFWorkbook(inputStream)
                else -> throw Exception() //TODO
            }

        var assignment: String? = null
        var headerColumn1: String? = null
        var headerColumn2: String? = null
        var headerColumn3: String? = null
        val sheet = workbook.getSheetAt(0)
        for ((i, row: Row) in sheet.withIndex()) {
            val listRow = ArrayList<String>()
            when (row.rowNum) {
                0 -> if (row.cellIterator().hasNext())
                    assignment = row.cellIterator().next().stringCellValue
                1 -> {
                    val cellIterator = row.cellIterator()
                    if (cellIterator != null) {
                        while (cellIterator.hasNext()) {
                            val cell = cellIterator.next()
                            when (cell.columnIndex) {
                                1 -> headerColumn1 = cell.stringCellValue
                                2 -> headerColumn2 = cell.stringCellValue
                                3 -> headerColumn3 = cell.stringCellValue
                            }
                        }
                    }
                }
                else -> {
                    val stringBuilder = StringBuilder()
                    val iterator = row.cellIterator()
                    while (iterator.hasNext()) {
                        val cell = iterator.next()
                        when (cell.columnIndex) {
                            1 -> listRow.add(cell.stringCellValue)
                            2 -> listRow.add(cell.stringCellValue)
                            in 3..9 -> {
                                if (cell.stringCellValue != "") {
                                    if (stringBuilder.toString() != "")
                                        stringBuilder.append("\n")
                                    stringBuilder.append(cell.stringCellValue)
                                }
                            }

                        }
                    }
                    listRow.add(stringBuilder.toString())
                }
            }
            dictionary.add(listRow)
        }
        return Dictionary(
            name = getFileName(excelFile),
            assignment = assignment,
            headerColumn1 = headerColumn1,
            headerColumn2 = headerColumn2,
            headerColumn3 = headerColumn3,
            dictionary = dictionary
        )
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                result =
                    cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            }
            cursor?.close()

        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    fun exportToPDF(dictionary: Dictionary) {
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
            BitmapFactory.decodeResource(context.resources, R.drawable.logo),
            null,
            RectF(247F, 770F, 347F, 820F),
            null)

        var height = 50
        canvas.translate(50f, 50f)

        if (dictionary.assignment != null) {
            val staticLayout = StaticLayout(
                dictionary.assignment, assignmentPaint, pageWidth - 100,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
            staticLayout.draw(canvas)
            canvas.translate(0f, (staticLayout.height + 30).toFloat())
            height += staticLayout.height + 30
        }

        if (
            dictionary.headerColumn1 != null &&
            dictionary.headerColumn2 != null &&
            dictionary.headerColumn3 != null
        ) {
            var staticLayout = StaticLayout(
                dictionary.headerColumn1, assignmentPaint, (pageWidth-150)/3,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
            staticLayout.draw(canvas)

            canvas.translate((pageWidth-150)/3 + 25F, 0F)
            staticLayout = StaticLayout(
                dictionary.headerColumn2, assignmentPaint, (pageWidth-150)/3,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
            staticLayout.draw(canvas)

            canvas.translate((pageWidth-150)/3 + 25F, 0F)
            staticLayout = StaticLayout(
                dictionary.headerColumn3, assignmentPaint, (pageWidth-150)/3,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
            staticLayout.draw(canvas)

            canvas.translate(-(((pageWidth-150)/3 + 25F)*2), (staticLayout.height + 30).toFloat())
            height += staticLayout.height + 30
        }

        for (row in dictionary.dictionary) {
            var column1 = ""
            var column2 = ""
            var column3 = ""
            for ((i,cell) in row.withIndex()) {
                when (i) {
                    0 -> column1 = cell
                    1 -> column2 = cell
                    2 -> column3 = cell
                }
            }

            val column1Layout = StaticLayout(
                column1, textPaint, (pageWidth-150)/3,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
            val column2Layout = StaticLayout(
                column2, textPaint, (pageWidth-150)/3,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
            val column3Layout = StaticLayout(
                column3, textPaint, (pageWidth-150)/3,
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
                        context.resources,
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

        val resolver: ContentResolver = context.contentResolver
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
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "1")
                .setSmallIcon(R.drawable.statusbaricon)
                .setContentTitle("PDF file generated successfully")
                .setContentText("Tap to open")
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(1, builder.build())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        pdfDocument.close()
    }

}
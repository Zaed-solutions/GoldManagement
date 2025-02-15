package com.zaed.common.ui.util

import android.graphics.Paint
import android.graphics.Typeface

object SheetUtil {

    // Extension functions
    private fun String.truncate(maxLength: Int = 20): String =
        if (length > maxLength) substring(0, maxLength) + "..." else this

    object PdfConfig {

        val minWidth = 28f
        val charWidth = 4.6f
        val cellHeight: Float = 20f
        val fontSize: Float = 9f
        val titleFontSize: Float = 16f

        val paint = Paint().apply {
            textSize = fontSize
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        val headerPaint = Paint().apply {
            textSize = fontSize
            typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }

        val footerPaint = Paint().apply {
            textSize = fontSize
            textAlign = Paint.Align.CENTER
            color = android.graphics.Color.DKGRAY
        }
        val titlePaint = Paint().apply {
            textSize = titleFontSize
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
        }

        val companyNamePaint = Paint().apply {
            textSize = fontSize
            textAlign = Paint.Align.LEFT
            isFakeBoldText = true
        }

        val borderPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }

        val blackBackground = Paint().apply {
            style = Paint.Style.FILL
            color = android.graphics.Color.LTGRAY
        }
    }

//    fun generatePaginatedArabicPdfReportForAllArrivals(
//        context: Context,
//        reservations: List<Reservation>,
//        fileName: String = "تقرير جميع الوصول",
//        title: String = "تقرير",
//    ): File {
//        // Initialize the PDF document
//        val pdfDocument = PdfDocument()
//        val headers: List<String> = listOf(
//            "#",
//            "اسم الضيف",
//            "اسم الشركة",
//            "التاريخ",
//            "الوقت",
//            "الحركة",
//            "السيارة",
//            "العدد",
//            "بداية الرحلة",
//            "نهاية الرحلة",
//            "السعر",
//            "التحصيل",
//            "الرصيد"
//        )
//
//
//        // Define column positions and widths
//        val columnData = listOf(
//            reservations.map { it.reservationNumber.toString().truncate() },
//            reservations.map { it.clientName.truncate() },
//            reservations.map { it.tourismCompany.truncate() },
//            reservations.map { it.date.formatEpochSecondsToDateNumbers() },
//            reservations.map { (it.time + it.date).formatEpochSecondsToTime()+"  " },
//            reservations.map { it.type.truncate() },
//            reservations.map { it.car +"  "},
//            reservations.map { it.peopleCount.toString()},
//            reservations.map { it.startLocation.truncate() },
//            reservations.map { it.endLocation.truncate() },
//            reservations.map { it.tourismRidePrice.toString() },
//            reservations.map {
//                if (it.travelCollectedAmount == 0) it.tourismCollectedAmount.toString()
//                    .truncate() else it.travelCollectedAmount.toString()
//            },
//            reservations.map { (it.tourismRidePrice - if (it.travelCollectedAmount == 0) it.tourismCollectedAmount else it.travelCollectedAmount).toString() }
//        )
//        val maxLengths = headers.mapIndexed { index, header ->
//            maxOf(
//                header.length,
//                columnData[index].maxOfOrNull { it.length } ?: 0
//            )
//        }
//        Log.d("SheetUtil", "generatePaginatedArabicPdfReportForAllArrivals: $maxLengths")
//
//        // Convert character lengths to column widths
//
//        val columnWidths = maxLengths.map { max(minWidth, it * charWidth) }
//
//        // Calculate the total width of the columns
//        val totalColumnsWidth = columnWidths.sum()
//
//        val pageWidth = totalColumnsWidth.toInt() + 50
//        val pageHeight = (pageWidth / 1.414f).toInt()
//        // Calculate the start position for the columns to center them on the page
//        val startXOffset = pageWidth - ((pageWidth - totalColumnsWidth) / 2)
//
//        // Create column start positions relative to the startXOffset
//        val columnStartPositions =
//            columnWidths.runningFold(startXOffset) { acc, width -> acc - width }
//
//        val maxRowsPerPage = ((pageHeight - 140) / cellHeight).toInt()
//
//        var currentPage = 1
//        var currentIndex = 0
//
//        // Track the totals for the last three columns
//        var totalPrice = reservations.sumOf { it.tourismRidePrice }
//        var totalCollected =
//            reservations.sumOf { max(it.tourismCollectedAmount, it.travelCollectedAmount) }
//        var totalBalance = totalPrice - totalCollected
//
//        while (currentIndex < reservations.size) {
//            // Start a new page
//            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPage).create()
//            val page = pdfDocument.startPage(pageInfo)
//            val canvas: Canvas = page.canvas
//
//            // Draw Title
//            canvas.drawText(
//                title,
//                (pageWidth / 2).toFloat(),
//                50f,
//                titlePaint
//            )
//
//            // Draw Header Row
//            var currentY = 70f
//            columnStartPositions.zip(columnWidths).forEachIndexed { index, (startX, width) ->
//                canvas.drawRect(
//                    startX - width,
//                    currentY,
//                    startX,
//                    currentY + cellHeight,
//                    blackBackground
//                )
//                canvas.drawText(
//                    headers[index],
//                    startX - (width / 2),
//                    currentY + (cellHeight / 2) + 4,
//                    headerPaint
//                )
//                canvas.drawRect(
//                    startX - width,
//                    currentY,
//                    startX,
//                    currentY + cellHeight,
//                    borderPaint
//                )
//            }
//            currentY += cellHeight
//
//            // Draw Data Rows
//            for (i in 0 until maxRowsPerPage) {
//                if (currentIndex >= reservations.size) break
//
//                val reservation = reservations[currentIndex]
//                val collectedPrice =
//                    max(reservation.tourismCollectedAmount, reservation.travelCollectedAmount)
//                val rowData = listOf(
//                    reservation.reservationNumber.toString(),
//                    reservation.clientName.truncate(),
//                    reservation.tourismCompany.truncate(),
//                    reservation.date.formatEpochSecondsToDateNumbers(),
//                    (reservation.time + reservation.date).formatEpochSecondsToTime(),
//                    reservation.type.truncate(),
//                    "${reservation.carCount} ${reservation.car}",
//                    reservation.peopleCount.toString(),
//                    reservation.startLocation.truncate(),
//                    reservation.endLocation.truncate(),
//                    reservation.tourismRidePrice.toString(), // السعر
//                    collectedPrice.toString(), // التحصيل
//                    (reservation.tourismRidePrice - collectedPrice).toString()
//                )
//                columnStartPositions.zip(columnWidths)
//                    .forEachIndexed { columnIndex, (startX, width) ->
//                        if (columnIndex == 0) {
//                            canvas.drawRect(
//                                startX - width,
//                                currentY,
//                                startX,
//                                currentY + cellHeight,
//                                blackBackground
//                            )
//                            canvas.drawText(
//                                rowData[columnIndex],
//                                startX - (width / 2),
//                                currentY + (cellHeight / 2) + 4,
//                                headerPaint
//                            )
//                            canvas.drawRect(
//                                startX - width,
//                                currentY,
//                                startX,
//                                currentY + cellHeight,
//                                borderPaint
//                            )
//                        } else {
//                            canvas.drawText(
//                                rowData[columnIndex],
//                                startX - (width / 2),
//                                currentY + (cellHeight / 2) + 4,
//                                paint
//                            )
//                            canvas.drawRect(
//                                startX - width,
//                                currentY,
//                                startX,
//                                currentY + cellHeight,
//                                borderPaint
//                            )
//                        }
//
//                    }
//                currentIndex++
//
//                currentY += cellHeight
//            }
//
//            // Draw Summary Row (Total)
//            if (currentIndex >= reservations.size || currentY + cellHeight > pageHeight) {
//                val summaryRowData = listOf(
//                    "",
//                    " عدد السجلات :${reservations.size}",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    totalPrice.toString(),
//                    totalCollected.toString(),
//                    totalBalance.toString()
//                )
//                columnStartPositions.zip(columnWidths)
//                    .forEachIndexed { columnIndex, (startX, width) ->
//                        if (columnIndex == 1) {
//                            val space = columnWidths.subList(1, 9).sum()
//                            canvas.drawRect(
//                                startX - space,
//                                currentY,
//                                startX,
//                                currentY + cellHeight,
//                                blackBackground
//                            )
//                            canvas.drawText(
//                                summaryRowData[columnIndex],
//                                startX - (space / 2),
//                                currentY + (cellHeight / 2) + 4,
//                                headerPaint
//                            )
//                            canvas.drawRect(
//                                startX - space,
//                                currentY,
//                                startX,
//                                currentY + cellHeight,
//                                borderPaint
//                            )
//                        } else if (columnIndex in 2..8) {
//                            return@forEachIndexed
//                        } else {
//                            canvas.drawRect(
//                                startX - width,
//                                currentY,
//                                startX,
//                                currentY + cellHeight,
//                                blackBackground
//                            )
//                            canvas.drawText(
//                                summaryRowData[columnIndex],
//                                startX - (width / 2),
//                                currentY + (cellHeight / 2) + 4,
//                                headerPaint
//                            )
//                            canvas.drawRect(
//                                startX - width,
//                                currentY,
//                                startX,
//                                currentY + cellHeight,
//                                borderPaint
//                            )
//                        }
//
//                    }
//            }
//
//            val footer = " صفحة $currentPage"
//            canvas.drawText(footer, (pageWidth / 2).toFloat(), pageHeight - 25f, footerPaint)
//
//
//            // Finish the current page
//            pdfDocument.finishPage(page)
//            currentPage++
//        }
//
//        // Save the PDF
//        val filePath = File(
//            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
//            "$fileName  ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())}.pdf"
//        )
//        pdfDocument.writeTo(FileOutputStream(filePath))
//        pdfDocument.close()
//
//        // Notify user
//        println("PDF report generated successfully at: $filePath")
//        return filePath // Return the generated file
//    }


    private fun List<String>.calculateMaxLengths(columnData: List<List<String>>): List<Int> {
        return this.mapIndexed { index, header ->
            maxOf(
                header.length,
                columnData[index].maxOfOrNull { it.length } ?: 0
            )
        }
    }



}
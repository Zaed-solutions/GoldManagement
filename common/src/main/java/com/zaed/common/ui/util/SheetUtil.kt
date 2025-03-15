package com.zaed.common.ui.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.pdf.PdfDocument
import android.os.Environment
import androidx.core.content.ContextCompat
import com.zaed.common.data.model.sale.DiscountType
import com.zaed.common.data.model.sale.StoreTransaction
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ReceiptUtil {
    // Modern PDF Configuration
    private object PdfConfig {
        // Dimensions
        val pageWidth = 595 // A4 width in points (1 point = 1/72 inch)
        val pageHeight = 842 // A4 height in points
        val margin = 40f
        val logoWidth = 120f
        val logoHeight = 80f

        // Typography
        val fontSizeSmall = 10f
        val fontSizeRegular = 12f
        val fontSizeMedium = 14f
        val fontSizeLarge = 18f
        val fontSizeTitle = 24f
        val lineSpacing = 18f
        val paragraphSpacing = 30f
        val cellPadding = 8f

        // Modern Color Palette
        val primaryColor = Color.parseColor("#3B1B00") // Deep Indigo
        val secondaryColor = Color.parseColor("#3B1B00") // Indigo
        val accentColor = Color.parseColor("#3B1B00") // Amber
        val lightAccentColor = Color.parseColor("#FCB882") // Light Amber
        val backgroundColor = Color.parseColor("#FFFFFF") // White
        val surfaceColor = Color.parseColor("#F5F5F5") // Light Gray
        val textPrimaryColor = Color.parseColor("#212121") // Almost Black
        val textSecondaryColor = Color.parseColor("#757575") // Dark Gray
        val textLightColor = Color.parseColor("#FFFFFF") // White

        // Paints with Arabic support
        val titlePaint = Paint().apply {
            textSize = fontSizeTitle
            color = textLightColor
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val headerPaint = Paint().apply {
            textSize = fontSizeLarge
            color = textLightColor
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val tableHeaderPaint = Paint().apply {
            textSize = fontSizeMedium
            color = textLightColor
            textAlign = Paint.Align.RIGHT
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val bodyPaint = Paint().apply {
            textSize = fontSizeRegular
            color = textPrimaryColor
            textAlign = Paint.Align.RIGHT
        }

        val labelPaint = Paint().apply {
            textSize = fontSizeRegular
            color = textPrimaryColor
            textAlign = Paint.Align.RIGHT
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val valuePaint = Paint().apply {
            textSize = fontSizeRegular
            color = textPrimaryColor
            textAlign = Paint.Align.LEFT
        }

        val totalsPaint = Paint().apply {
            textSize = fontSizeMedium
            color = textPrimaryColor
            textAlign = Paint.Align.RIGHT
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val footerPaint = Paint().apply {
            textSize = fontSizeSmall
            color = textSecondaryColor
            textAlign = Paint.Align.CENTER
        }

        // Background Paints
        val backgroundPaint = Paint().apply {
            color = backgroundColor
        }

        val surfacePaint = Paint().apply {
            color = surfaceColor
        }
    }

    private fun mipmapResourceToBitmap(context: Context, mipmapId: Int): Bitmap? {
        try {
            // ContextCompat.getDrawable works for both drawable and mipmap resources
            val drawable = ContextCompat.getDrawable(context, mipmapId) ?: return null

            if (drawable is BitmapDrawable) {
                return drawable.bitmap
            }

            // Create bitmap with intrinsic width and height if available
            val width =
                if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else PdfConfig.logoWidth.toInt()
            val height =
                if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else PdfConfig.logoHeight.toInt()

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * Generates a modern PDF receipt for a StoreSale in Arabic with professional design.
     */
    fun generateStoreSaleReceipt(
        context: Context,
        storeSale: StoreTransaction,
        logoMipmapId: Int? = null,
        fileName: String = "إيصال_بيع"
    ): File {

        val logo = logoMipmapId?.let { mipmapResourceToBitmap(context, it) }
        // Initialize PDF document
        val pdfDocument = PdfDocument()
        val pageInfo =
            PdfDocument.PageInfo.Builder(PdfConfig.pageWidth, PdfConfig.pageHeight, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        // Fill background
        canvas.drawRect(
            0f,
            0f,
            PdfConfig.pageWidth.toFloat(),
            PdfConfig.pageHeight.toFloat(),
            PdfConfig.backgroundPaint
        )

        // Header section with gradient
        val headerGradient = LinearGradient(
            0f, 0f, 0f, 150f,
            PdfConfig.primaryColor, PdfConfig.secondaryColor, Shader.TileMode.CLAMP
        )
        val headerPaint = Paint().apply {
            shader = headerGradient
        }
        canvas.drawRect(0f, 0f, PdfConfig.pageWidth.toFloat(), 200f, headerPaint)

        // Add logo
        var currentY = 20f
        if (logo != null) {
            val centerX = (PdfConfig.pageWidth - PdfConfig.logoWidth) / 2
            // If logo dimensions don't match config, scale it
            if (logo.width != PdfConfig.logoWidth.toInt() || logo.height != PdfConfig.logoHeight.toInt()) {
                val scaledLogo = Bitmap.createScaledBitmap(
                    logo,
                    PdfConfig.logoWidth.toInt(),
                    PdfConfig.logoHeight.toInt(),
                    true
                )
                canvas.drawBitmap(scaledLogo, centerX, currentY, null)
            } else {
                canvas.drawBitmap(logo, centerX, currentY, null)
            }
            currentY += PdfConfig.logoHeight + 20f
        } else {
            currentY += 20f
        }

        // Title - "إيصال بيع"
        canvas.drawText("إيصال بيع", PdfConfig.pageWidth / 2f, currentY, PdfConfig.titlePaint)
        currentY += PdfConfig.paragraphSpacing + 20f

        // Store name
        canvas.drawText(
            storeSale.storeName,
            PdfConfig.pageWidth / 2f,
            currentY,
            PdfConfig.headerPaint
        )
        currentY += PdfConfig.paragraphSpacing + 25f

        // Receipt details box
        val detailsStartY = currentY
        val detailsWidth = PdfConfig.pageWidth - (2 * PdfConfig.margin)
        val detailsRect = RoundedRect(
            PdfConfig.margin,
            detailsStartY,
            PdfConfig.margin + detailsWidth,
            detailsStartY + 50f,
            20f
        )
        drawRoundedRect(canvas, detailsRect, PdfConfig.surfacePaint)

        // Left column - Employee and Customer info
        val leftCol = PdfConfig.margin + detailsWidth - 20f
        val rightCol = leftCol - 155f
        currentY = detailsStartY + 25f

//        // Employee and customer details
//        drawLabelValuePair(canvas, "الموظف:", storeSale.employeeName, leftCol, currentY, rightCol)
//        currentY += PdfConfig.lineSpacing + 5f

        drawLabelValuePair(canvas, "العميل:", storeSale.customerName, leftCol, currentY, rightCol)
        currentY += PdfConfig.lineSpacing + 5f

//        if (storeSale.customerPhoneNumber.isNotEmpty()) {
//            drawLabelValuePair(
//                canvas,
//                "رقم الهاتف:",
//                storeSale.customerPhoneNumber,
//                leftCol,
//                currentY,
//                rightCol
//            )
//            currentY += PdfConfig.lineSpacing + 5f
//        }

        // Right column - Date and Receipt Number
        currentY = detailsStartY + 25f
        val dateStr =
            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(storeSale.createdAt)
        val rightColumnX = PdfConfig.margin + detailsWidth / 2 - 20f
        val rightValueX = rightColumnX - 155

        drawLabelValuePair(canvas, "التاريخ:", dateStr, rightColumnX, currentY, rightValueX)
        currentY += PdfConfig.lineSpacing + 5f

        // Generate receipt number (you might want to use a real one from your data)
        val receiptNumber = "CR-${storeSale.receiptNumber}"
        drawLabelValuePair(
            canvas,
            "رقم الإيصال:",
            receiptNumber,
            rightColumnX,
            currentY,
            rightValueX
        )

        // Move to products table
        currentY = detailsStartY + 70f

        // Products Table Header with accent color
        val tableHeaderPaint = Paint().apply {
            color = PdfConfig.accentColor
        }
        val tableWidth = PdfConfig.pageWidth - (2 * PdfConfig.margin)
        val tableHeaderRect = RoundedRect(
            PdfConfig.margin,
            currentY,
            PdfConfig.margin + tableWidth,
            currentY + 40f,
            10f
        )
        drawRoundedRect(canvas, tableHeaderRect, tableHeaderPaint)

        // Table header text
        val tableHeader = listOf("المنتج", "الجرام", "الإجمالي")

        // Create a list of all rows including products
        val allRows = mutableListOf<List<String>>()
        storeSale.products.forEach { product ->
            allRows.add(
                listOf(
                    product.name,
                    "${product.grams}g",
                    "${product.grams * product.gramPrice} ر.س"
                )
            )
        }

        // Calculate optimal column widths
        val columnWidths = calculateColumnWidths(
            tableHeader,
            allRows,
            PdfConfig.tableHeaderPaint,
            PdfConfig.bodyPaint,
            tableWidth = tableWidth
        )

        // Use the calculated columnWidths for the header
        drawTableRow(
            canvas,
            tableHeader,
            columnWidths,
            PdfConfig.margin + tableWidth,
            currentY + 25f,
            PdfConfig.tableHeaderPaint
        )
        currentY += 40f

        // Product rows with subtle alternating background
        val rowHeight = PdfConfig.lineSpacing + PdfConfig.cellPadding * 2
        storeSale.products.forEachIndexed { index, product ->
            // Row background
            val rowBackgroundPaint = Paint().apply {
                color = if (index % 2 == 0) PdfConfig.surfaceColor else PdfConfig.backgroundColor
            }
            canvas.drawRect(
                PdfConfig.margin,
                currentY,
                PdfConfig.margin + tableWidth,
                currentY + rowHeight,
                rowBackgroundPaint
            )

            // Row data
            val rowData = listOf(
                product.name,
                "${product.grams}g",
                (product.grams * product.gramPrice).toMoneyFormat(2)
            )
            drawTableRow(
                canvas,
                rowData,
                columnWidths,
                PdfConfig.margin + tableWidth,
                currentY + PdfConfig.lineSpacing,
                PdfConfig.bodyPaint
            )
            currentY += rowHeight
        }

        // Calculate subtotal
        val subtotal = storeSale.products.sumOf { it.grams * it.gramPrice }

        // Inside the generateStoreSaleReceipt function, replace the current totals section with this code:

// Summary Table
        currentY += 30f
        val summaryTableWidth = 300f
        val summaryStartX = PdfConfig.pageWidth - PdfConfig.margin - summaryTableWidth

// Summary Table Header
        val summaryHeaderPaint = Paint(PdfConfig.tableHeaderPaint)
        val summaryHeaderRect = RoundedRect(
            summaryStartX,
            currentY,
            summaryStartX + summaryTableWidth,
            currentY + 40f,
            10f
        )
        drawRoundedRect(canvas, summaryHeaderRect, tableHeaderPaint)

// Draw "ملخص الفاتورة" (Invoice Summary) in the header
        canvas.drawText(
            "ملخص الفاتورة",
            summaryStartX + summaryTableWidth / 2,
            currentY + 25f,
            PdfConfig.headerPaint.apply { textAlign = Paint.Align.CENTER }
        )
        currentY += 40f


// Summary Table Rows with subtle alternating background
        val summaryRowHeight = PdfConfig.lineSpacing + PdfConfig.cellPadding * 2
        val summaryData = mutableListOf<List<String>>()

// Add subtotal row
        summaryData.add(listOf("المجموع الفرعي:", "$subtotal ر.س"))

// Add discount row
        val discountText = when (storeSale.discount.type) {
            DiscountType.PERCENTAGE -> "${storeSale.discount.value}%"
            DiscountType.AMOUNT -> "${storeSale.discount.value.toMoneyFormat(2)} "
            else -> ""
        }
        if (discountText.isNotEmpty()) {
            summaryData.add(listOf("الخصم:", discountText))
        }

// Add tax row if applicable (example)
// summaryData.add(listOf("الضريبة (15%):", "${subtotal * 0.15} ر.س"))

// Calculate column widths for summary table
        val summaryColumnWidths = listOf(summaryTableWidth * 0.6f, summaryTableWidth * 0.4f)

// Draw summary rows
        summaryData.forEachIndexed { index, rowData ->
            // Row background
            val rowBackgroundPaint = Paint().apply {
                color = if (index % 2 == 0) PdfConfig.surfaceColor else PdfConfig.backgroundColor
            }
            canvas.drawRect(
                summaryStartX,
                currentY,
                summaryStartX + summaryTableWidth,
                currentY + summaryRowHeight,
                rowBackgroundPaint
            )

            // Row data
            drawTableRow(
                canvas,
                rowData,
                summaryColumnWidths,
                summaryStartX + summaryTableWidth,
                currentY + PdfConfig.lineSpacing,
                PdfConfig.bodyPaint
            )
            currentY += summaryRowHeight
        }

// Grand Total with accent background
        val grandTotalRect = RoundedRect(
            summaryStartX,
            currentY,
            summaryStartX + summaryTableWidth,
            currentY + summaryRowHeight,
            10f
        )
        val totalBackgroundPaint = Paint().apply {
            color = PdfConfig.lightAccentColor
        }
        drawRoundedRect(canvas, grandTotalRect, totalBackgroundPaint)

// Grand Total text
        val grandTotalPaint = Paint(PdfConfig.totalsPaint).apply {
            textSize = PdfConfig.fontSizeLarge
        }
        drawTableRow(
            canvas,
            listOf("الإجمالي النهائي:", "${storeSale.totalAmount.toMoneyFormat(2)}"),
            summaryColumnWidths,
            summaryStartX + summaryTableWidth,
            currentY + PdfConfig.lineSpacing + 5,
            grandTotalPaint
        )
        currentY += summaryRowHeight + 20f


        // Footer section
        val footerStartY = PdfConfig.pageHeight - 100f
        val footerGradient = LinearGradient(
            0f, footerStartY, 0f, PdfConfig.pageHeight.toFloat(),
            PdfConfig.secondaryColor, PdfConfig.primaryColor, Shader.TileMode.CLAMP
        )
        val footerPaint = Paint().apply {
            shader = footerGradient
        }
        canvas.drawRect(
            0f,
            footerStartY,
            PdfConfig.pageWidth.toFloat(),
            PdfConfig.pageHeight.toFloat(),
            footerPaint
        )

        // Footer text
        canvas.drawText(
            "شكرًا لاختيارك متجرنا!",
            PdfConfig.pageWidth / 2f,
            footerStartY + 30f,
            PdfConfig.headerPaint
        )
        canvas.drawText(
            "للتواصل: ${storeSale.storeName}",
            PdfConfig.pageWidth / 2f,
            footerStartY + 55f,
            PdfConfig.footerPaint.apply { color = PdfConfig.textLightColor })


        // Finish the page
        pdfDocument.finishPage(page)

        // Save the PDF
        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "$fileName ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())}.pdf"
        )
        pdfDocument.writeTo(FileOutputStream(filePath))
        pdfDocument.close()
        return filePath
    }

    /**
     * Draws a table row with right-aligned text.
     */
    private fun drawTableRow(
        canvas: Canvas,
        rowData: List<String>,
        columnWidths: List<Float>,
        startX: Float,
        startY: Float,
        paint: Paint
    ) {
        var currentX = startX
        rowData.forEachIndexed { index, text ->
            if (index < columnWidths.size) {
                // Calculate the correct position based on text alignment
                val textX = when (paint.textAlign) {
                    Paint.Align.RIGHT -> currentX - PdfConfig.cellPadding
                    Paint.Align.CENTER -> currentX - (columnWidths[index] / 2)
                    else -> currentX + PdfConfig.cellPadding  // LEFT alignment
                }

                canvas.drawText(text, textX, startY, paint)
                currentX -= columnWidths[index]
            }
        }
    }


    /**
     * Draws a label-value pair with right-aligned label and left-aligned value.
     */
    private fun drawLabelValuePair(
        canvas: Canvas,
        label: String,
        value: String,
        labelX: Float,
        y: Float,
        valueX: Float,
        labelPaint: Paint = PdfConfig.labelPaint,
        valuePaint: Paint = PdfConfig.valuePaint
    ) {
        canvas.drawText(label, labelX, y, labelPaint.apply { textAlign = Paint.Align.RIGHT })
        canvas.drawText(value, valueX, y, valuePaint.apply { textAlign = Paint.Align.LEFT })
    }

    /**
     * Helper class to represent a rectangle with rounded corners.
     */
    private data class RoundedRect(
        val left: Float,
        val top: Float,
        val right: Float,
        val bottom: Float,
        val cornerRadius: Float
    )

    /**
     * Draws a rectangle with rounded corners.
     */
    private fun drawRoundedRect(canvas: Canvas, rect: RoundedRect, paint: Paint) {
        canvas.drawRoundRect(
            rect.left,
            rect.top,
            rect.right,
            rect.bottom,
            rect.cornerRadius,
            rect.cornerRadius,
            paint
        )
    }

    /**
     * Measures text width for a given string and paint.
     */
    private fun measureTextWidth(text: String, paint: Paint): Float {
        return paint.measureText(text)
    }

    /**
     * Calculates optimal column widths based on content.
     */
    private fun calculateColumnWidths(
        header: List<String>,
        allRows: List<List<String>>,
        headerPaint: Paint,
        bodyPaint: Paint,
        minWidth: Float = 80f,  // Minimum width for any column
        maxWidth: Float = 300f,  // Maximum width for any column
        tableWidth: Float       // Total available table width
    ): List<Float> {
        val columns = header.size
        val maxWidths = FloatArray(columns) { 0f }

        // Check header widths
        header.forEachIndexed { index, text ->
            maxWidths[index] = maxOf(maxWidths[index], measureTextWidth(text, headerPaint))
        }

        // Check all data rows
        allRows.forEach { row ->
            row.forEachIndexed { index, text ->
                if (index < columns) {
                    maxWidths[index] = maxOf(maxWidths[index], measureTextWidth(text, bodyPaint))
                }
            }
        }

        // Apply min/max constraints and add padding
        val padding = 20f  // Padding on each side of text
        maxWidths.forEachIndexed { index, width ->
            maxWidths[index] = minOf(maxOf(width + (padding * 2), minWidth), maxWidth)
        }

        // If sum of maxWidths is less than tableWidth, distribute extra space proportionally
        val totalWidth = maxWidths.sum()
        if (totalWidth < tableWidth) {
            val extraPerColumn = (tableWidth - totalWidth) / columns
            maxWidths.indices.forEach { i -> maxWidths[i] += extraPerColumn }
        }
        // If sum of maxWidths is more than tableWidth, scale down proportionally
        else if (totalWidth > tableWidth) {
            val scale = tableWidth / totalWidth
            maxWidths.indices.forEach { i -> maxWidths[i] *= scale }
        }

        return maxWidths.toList()
    }
}


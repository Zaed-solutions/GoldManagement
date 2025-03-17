package com.zaed.common.util

import android.content.Context
import com.dantsu.escposprinter.connection.DeviceConnection
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.dantsu.escposprinter.exceptions.EscPosConnectionException
import com.zaed.common.util.AsyncEscPosPrint.Companion.FINISH_NO_PRINTER
import com.zaed.common.util.AsyncEscPosPrint.Companion.PROGRESS_CONNECTING

class AsyncBluetoothEscPosPrint(
    context: Context,
    onPrintFinished: OnPrintFinished? = null
) : AsyncEscPosPrint(context, onPrintFinished){

    override fun doInBackground(vararg printersData: AsyncEscPosPrinter): PrinterStatus {
        if (printersData.isEmpty()) {
            return PrinterStatus(null, FINISH_NO_PRINTER)
        }

        val printerData = printersData[0]
        val deviceConnection = printerData.printerConnection

        publishProgress(PROGRESS_CONNECTING)

        // Create a new array instead of modifying the existing one
        val updatedPrinters = if (deviceConnection == null) {
            val newPrinter = AsyncEscPosPrinter(
                BluetoothPrintersConnections.selectFirstPaired()!!,
                printerData.getPrinterDpi(),
                printerData.getPrinterWidthMM(),
                printerData.getPrinterNbrCharactersPerLine()
            ).apply {
                textsToPrint = printerData.textsToPrint
            }
            arrayOf(newPrinter)
        } else {
            try {
                deviceConnection.connect()
            } catch (e: EscPosConnectionException) {
                e.printStackTrace()
            }
            printersData
        }

        // Pass the updated array to super
        return super.doInBackground(*updatedPrinters)
    }
}
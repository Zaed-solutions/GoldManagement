package com.zaed.common.util

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import com.dantsu.escposprinter.EscPosCharsetEncoding
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException
import com.dantsu.escposprinter.exceptions.EscPosConnectionException
import com.dantsu.escposprinter.exceptions.EscPosEncodingException
import com.dantsu.escposprinter.exceptions.EscPosParserException
import java.lang.ref.WeakReference

abstract class AsyncEscPosPrint(
    context: Context,
    protected var onPrintFinished: OnPrintFinished? = null
) : AsyncTask<AsyncEscPosPrinter, Int, AsyncEscPosPrint.PrinterStatus>() {
    
    companion object {
        const val FINISH_SUCCESS = 1
        const val FINISH_NO_PRINTER = 2
        const val FINISH_PRINTER_DISCONNECTED = 3
        const val FINISH_PARSER_ERROR = 4
        const val FINISH_ENCODING_ERROR = 5
        const val FINISH_BARCODE_ERROR = 6

        const val PROGRESS_CONNECTING = 1
        const val PROGRESS_CONNECTED = 2
        const val PROGRESS_PRINTING = 3
        const val PROGRESS_PRINTED = 4
    }

    protected var dialog: ProgressDialog? = null
    protected val weakContext: WeakReference<Context> = WeakReference(context)

    override fun doInBackground(vararg printersData: AsyncEscPosPrinter): PrinterStatus {
        if (printersData.isEmpty()) {
            return PrinterStatus(null, FINISH_NO_PRINTER)
        }

        publishProgress(PROGRESS_CONNECTING)

        val printerData = printersData[0]

        try {
            val deviceConnection = printerData.printerConnection

            if (deviceConnection == null) {
                return PrinterStatus(null, FINISH_NO_PRINTER)
            }

            val printer = EscPosPrinter(
                deviceConnection,
                printerData.getPrinterDpi(),
                printerData.getPrinterWidthMM(),
                printerData.getPrinterNbrCharactersPerLine(),
                EscPosCharsetEncoding("windows-1252", 16)
            )

            // printer.useEscAsteriskCommand(true)

            publishProgress(PROGRESS_PRINTING)

            val textsToPrint = printerData.textsToPrint

            for (textToPrint in textsToPrint) {
                printer.printFormattedTextAndCut(textToPrint)
                Thread.sleep(500)
            }

            publishProgress(PROGRESS_PRINTED)

        } catch (e: EscPosConnectionException) {
            e.printStackTrace()
            return PrinterStatus(printerData, FINISH_PRINTER_DISCONNECTED)
        } catch (e: EscPosParserException) {
            e.printStackTrace()
            return PrinterStatus(printerData, FINISH_PARSER_ERROR)
        } catch (e: EscPosEncodingException) {
            e.printStackTrace()
            return PrinterStatus(printerData, FINISH_ENCODING_ERROR)
        } catch (e: EscPosBarcodeException) {
            e.printStackTrace()
            return PrinterStatus(printerData, FINISH_BARCODE_ERROR)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        
        return PrinterStatus(printerData, FINISH_SUCCESS)
    }

    override fun onPreExecute() {
        val context = weakContext.get() ?: return

        dialog = ProgressDialog(context).apply {
            setTitle("Printing in progress...")
            setMessage("...")
            setProgressNumberFormat("%1d / %2d")
            setCancelable(false)
            isIndeterminate = false
            setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            show()
        }
    }

    override fun onProgressUpdate(vararg progress: Int?) {
        dialog?.apply {
            when (progress[0]) {
                PROGRESS_CONNECTING -> setMessage("Connecting printer...")
                PROGRESS_CONNECTED -> setMessage("Printer is connected...")
                PROGRESS_PRINTING -> setMessage("Printer is printing...")
                PROGRESS_PRINTED -> setMessage("Printer has finished...")
            }
            setProgress(progress[0] ?: 0)
            max = 4
        }
    }

    override fun onPostExecute(result: PrinterStatus) {
        dialog?.dismiss()
        dialog = null

        val context = weakContext.get() ?: return

        when (result.printerStatus) {
            FINISH_SUCCESS -> AlertDialog.Builder(context)
                .setTitle("Success")
                .setMessage("Congratulation ! The texts are printed !")
                .show()
            FINISH_NO_PRINTER -> AlertDialog.Builder(context)
                .setTitle("No printer")
                .setMessage("The application can't find any printer connected.")
                .show()
            FINISH_PRINTER_DISCONNECTED -> AlertDialog.Builder(context)
                .setTitle("Broken connection")
                .setMessage("Unable to connect the printer.")
                .show()
            FINISH_PARSER_ERROR -> AlertDialog.Builder(context)
                .setTitle("Invalid formatted text")
                .setMessage("It seems to be an invalid syntax problem.")
                .show()
            FINISH_ENCODING_ERROR -> AlertDialog.Builder(context)
                .setTitle("Bad selected encoding")
                .setMessage("The selected encoding character returning an error.")
                .show()
            FINISH_BARCODE_ERROR -> AlertDialog.Builder(context)
                .setTitle("Invalid barcode")
                .setMessage("Data send to be converted to barcode or QR code seems to be invalid.")
                .show()
        }
        
        onPrintFinished?.let {
            if (result.printerStatus == FINISH_SUCCESS) {
                it.onSuccess(result.asyncEscPosPrinter)
            } else {
                it.onError(result.asyncEscPosPrinter, result.printerStatus)
            }
        }
    }

    data class PrinterStatus(
        val asyncEscPosPrinter: AsyncEscPosPrinter?,
        val printerStatus: Int
    )

    abstract class OnPrintFinished {
        abstract fun onError(asyncEscPosPrinter: AsyncEscPosPrinter?, codeException: Int)
        abstract fun onSuccess(asyncEscPosPrinter: AsyncEscPosPrinter?)
    }
}
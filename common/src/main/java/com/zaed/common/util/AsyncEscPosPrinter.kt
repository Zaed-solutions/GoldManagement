package com.zaed.common.util

import com.dantsu.escposprinter.EscPosPrinterSize
import com.dantsu.escposprinter.connection.DeviceConnection

class AsyncEscPosPrinter(
    val printerConnection: DeviceConnection,
    printerDpi: Int,
    printerWidthMM: Float,
    printerNbrCharactersPerLine: Int
) : EscPosPrinterSize(printerDpi, printerWidthMM, printerNbrCharactersPerLine) {
    var textsToPrint: Array<String> = emptyArray()
    fun addTextToPrint(textToPrint: String): AsyncEscPosPrinter {
        val tmp = Array(this.textsToPrint.size + 1) { index ->
            if (index < this.textsToPrint.size) this.textsToPrint[index] else textToPrint
        }
        this.textsToPrint = tmp
        return this
    }
}
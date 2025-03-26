package com.zaed.common.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.DisplayMetrics
import com.dantsu.escposprinter.connection.DeviceConnection
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import com.zaed.common.R
import com.zaed.common.data.model.sale.StoreTransaction
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format


@SuppressLint("SimpleDateFormat")
fun getAsyncEscPosPrinter(
    context: Context,
    printerConnection: DeviceConnection,
    sale: StoreTransaction
): AsyncEscPosPrinter {
    val printer = AsyncEscPosPrinter(
        printerConnection,
        203,
        58f,
        32
    )
    return printer.addTextToPrint(
        """
            [C]<img>${
            PrinterTextParserImg.bitmapToHexadecimalString(
                printer,
                context.resources
                    .getDrawableForDensity(
                        R.drawable.receipt_header_removebg_preview,
                        DisplayMetrics.DENSITY_MEDIUM
                    )
            )
        }</img>
            [L]
            [C]<u><font size='big'>ORDER N°${sale.receiptNumber}</font></u>
            [L]
            [C]<u type='double'>${sale.createdAt.format(DateFormat.DATE_TIME)}</u>
            [C]
            [C]================================
            ${
            sale.products.forEach {
                """
                [L]<b>${it.quantity} x ${it.name}</b>[R]${it.totalPriceBeforeDiscount}$
                [L]
                """.trimIndent()
            }
        }
            [C]--------------------------------
            ${
            if (sale.totalDiscount > 0)
                """
                [R]SUBTOTAL PRICE :[R]${sale.totalAmountBeforeDiscount}$
                [R]DISCOUNT :[R]${sale.totalDiscount}$
                """.trimIndent()
            else
                ""
        }
            [R]TOTAL PRICE :[R]${sale.totalAmount}$
            [L]
            [C]================================
            ${
            if (sale.accountId.isNotBlank()) {
                """
                    [L]
                    [L]<font size='tall'>Customer :</font>
                    [L]${sale.customerName}
                    [L]${sale.storeName}, ${sale.storeLocation}
                    ${if (sale.customerPhone.isNotBlank()) {
                        """
                        [L]Tel : ${sale.customerPhone}
                        """.trimIndent()
                    } else ""
                    }
                """.trimIndent()
            } else ""
        }
            """.trimIndent()
    )
}

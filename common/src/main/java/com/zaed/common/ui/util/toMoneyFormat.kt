package com.zaed.common.ui.util

import java.text.NumberFormat
import java.util.Locale

fun Number.toMoneyFormat(padding: Int = 0): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
        minimumFractionDigits = padding
        maximumFractionDigits = padding
    }
    val formattedAmount = formatter.format(this)
    return formattedAmount
}
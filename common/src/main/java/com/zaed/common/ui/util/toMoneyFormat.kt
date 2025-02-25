package com.zaed.common.ui.util

import java.text.NumberFormat

fun Number.toMoneyFormat(padding: Int = 0): String {
    val formatter = NumberFormat.getCurrencyInstance().apply {
        minimumFractionDigits = padding
        maximumFractionDigits = padding
    }
    val formattedAmount = formatter.format(this)
    return formattedAmount
}
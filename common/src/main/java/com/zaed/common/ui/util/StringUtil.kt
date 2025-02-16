package com.zaed.common.ui.util

import java.text.NumberFormat
import java.util.Locale

fun Number.formatMoney(padding: Int = 0): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault()).apply {
        minimumFractionDigits = padding
        maximumFractionDigits = padding
    }
    val formattedAmount = formatter.format(this)
    return formattedAmount
}

fun String.isValidEmail() =
    isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPhoneNumber() =
    isNotEmpty() && android.util.Patterns.PHONE.matcher(this).matches()
package com.zaed.common.ui.util

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
fun Number.toMoneyFormat(padding: Int = 0): String {
    val locale = when (Locale.getDefault().language) {
        "ar" -> Locale("ar", "MA") // Arabic (Morocco)
        "fr" -> Locale.FRENCH // French (France is fine since Morocco follows similar formatting)
        else -> Locale("en", "MA") // English (Morocco)
    }

    val formatter = NumberFormat.getCurrencyInstance(locale).apply {
        currency = Currency.getInstance("MAD") // Explicitly set Moroccan Dirham
        minimumFractionDigits = padding
        maximumFractionDigits = padding
    }

    return formatter.format(this)
}
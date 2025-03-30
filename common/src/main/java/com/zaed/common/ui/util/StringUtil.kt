package com.zaed.common.ui.util

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

fun Number.formatMoney(padding: Int = 1): String {
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

fun String.isValidEmail() =
    isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPhoneNumber() =
    isNotEmpty() && android.util.Patterns.PHONE.matcher(this).matches()
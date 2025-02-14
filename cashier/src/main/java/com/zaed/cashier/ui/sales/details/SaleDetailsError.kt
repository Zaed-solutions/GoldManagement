package com.zaed.cashier.ui.sales.details

import android.util.Log
import androidx.annotation.StringRes

sealed class SaleDetailsError(
    @StringRes open val userMessage: Int = 0
) : Exception() {
    data class ErrorType(
        @StringRes override val userMessage: Int = 0,
    ) : SaleDetailsError()

    data class CustomError(
        @StringRes override val userMessage: Int = 0,
        val reason: String = "",
        val location: String = "",
    ) : SaleDetailsError()
}

fun SaleDetailsError.CustomError.log() = Log.d(location, reason)
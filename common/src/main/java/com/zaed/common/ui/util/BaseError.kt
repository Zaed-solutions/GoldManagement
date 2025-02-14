package com.zaed.common.ui.util

import android.util.Log
import androidx.annotation.StringRes
import com.zaed.common.R

sealed class BaseError(
    @StringRes open val userMessage: Int = 0,
    open val location: String = getClassAndFunctionName(),
) : Exception() {
    data class Unknown(
        @StringRes override val userMessage: Int = R.string.unknown_error,
        override val location: String = "",
        val reason: String = ""
    ): BaseError(userMessage, location) {
        fun log() = Log.e("Error at $location", "Reason: $reason")
        }
}

fun getClassAndFunctionName(): String {
    return Thread.currentThread().stackTrace
        .firstOrNull { it.className.contains("com.zaed") } // Filter by package name
        ?.let { "${it.className.substringAfterLast('.')}::${it.methodName}" }
        ?: "UnknownLocation"
}
enum class LossErrorType(
    @StringRes val userMessage: Int
) {
    NETWORK_FAILURE(
        R.string.network_failure
    ),
}
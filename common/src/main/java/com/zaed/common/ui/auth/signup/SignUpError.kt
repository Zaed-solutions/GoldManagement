package com.zaed.common.ui.auth.signup

import android.util.Log
import androidx.annotation.StringRes
import com.zaed.common.R

sealed class SignUpError(
    @StringRes open val userMessage: Int = R.string.sign_up_failed
) : Exception() {
    data class UserAlreadyExists(
        @StringRes override val userMessage: Int = R.string.user_already_exists,
    ): SignUpError()

    data class SignUpFailed (
        @StringRes override val userMessage: Int = R.string.sign_up_failed,
        val reason: String = "",
        val location: String = "",
    ): SignUpError()
}

fun SignUpError.SignUpFailed.log()= Log.d(location,reason)


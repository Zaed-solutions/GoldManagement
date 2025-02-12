package com.zaed.common.data.model.ui

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

sealed class LoginError(
    @StringRes open val userMessage: Int = R.string.login_failed
) : Exception(){
    data class UserNotFound(
        @StringRes override val userMessage: Int = R.string.user_not_found,
    ): LoginError()

    data class LoginFailed(
        @StringRes override val userMessage: Int = R.string.login_failed,
        val reason: String = "",
        val location: String = "",
    ): LoginError()
}
fun LoginError.LoginFailed.log()= Log.d(location,reason)
package com.zaed.common.ui.component.auth.login

import android.util.Log
import androidx.annotation.StringRes
import com.zaed.common.R

sealed class LoginError(
    @StringRes open val userMessage: Int = R.string.login_failed
) : Exception(){
    data class UserNotFound(
        @StringRes override val userMessage: Int = R.string.user_not_found,
    ): LoginError()

    data class InCorrectPassword(
        @StringRes override val userMessage: Int = R.string.invalid_password,
    ): LoginError()

    data class InvalidRole(
        @StringRes override val userMessage: Int = R.string.invalid_role,
    ): LoginError()

    data class LoginFailed(
        @StringRes override val userMessage: Int = R.string.login_failed,
        val reason: String = "",
        val location: String = "",
    ): LoginError()
}
fun LoginError.LoginFailed.log()= Log.d(location,reason)
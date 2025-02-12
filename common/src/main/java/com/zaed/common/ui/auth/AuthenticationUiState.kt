package com.zaed.common.ui.auth

import androidx.annotation.StringRes
import com.zaed.common.R
import com.zaed.common.data.model.User
import com.zaed.common.data.model.UserRole

data class AuthenticationUiState(
    val fullName: String = "",
    val userName: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val role: UserRole = UserRole.NONE,
    val user: User? = null,
    val fieldsError: FieldsError = FieldsError.NONE,
    val errorMessage: Exception? = null,
    val successMessage: String? = null
)




enum class FieldsError(@StringRes val message: Int) {
    NONE(0),
    EMPTY_FULL_NAME(R.string.empty_full_name),
    INVALID_FULL_NAME(R.string.invalid_full_name),
    EMPTY_USER_NAME(R.string.empty_user_name),
    INVALID_USER_NAME(R.string.invalid_user_name),
    EMPTY_PASSWORD(R.string.empty_password),
    INVALID_PASSWORD(R.string.invalid_password)
}
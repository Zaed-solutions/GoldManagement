package com.zaed.cashier.ui.loss

import androidx.annotation.StringRes
import com.zaed.common.R
import com.zaed.common.data.model.loss.Loss
import com.zaed.common.data.model.authentication.User

data class LossUiState(
    val isSignedOut: Boolean = false,
    val losses: Map<String, List<Loss>> = emptyMap(),
    val currentUser: User = User(),
    val isLoading: Boolean = false,
    @StringRes val errorMessage: Int? = null,
    val fieldError: LossFieldsError = LossFieldsError.NONE,
    val successMessage: String? = null
)

enum class LossFieldsError(@StringRes val message: Int) {
    NONE(0),
    LOSS_VALUE_IS_EMPTY(R.string.loss_value_is_empty),
    LOSS_REASON_IS_EMPTY(R.string.loss_reason_is_empty),
    LOSS_VALUE_IS_INVALID(R.string.loss_value_is_invalid),
}
package com.zaed.cashier.ui.loss.display

import androidx.annotation.StringRes
import com.zaed.cashier.R
import com.zaed.cashier.data.model.Loss

data class LossUiState(
    val losses: List<Loss> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: LossError? = null,
    val fieldError: LossFieldsError = LossFieldsError.NONE,
    val successMessage: String? = null
)

enum class LossFieldsError(@StringRes val message: Int) {
    NONE(0),
    LOSS_VALUE_IS_EMPTY(R.string.loss_value_is_empty),
    LOSS_REASON_IS_EMPTY(R.string.loss_reason_is_empty),
    LOSS_VALUE_IS_INVALID(R.string.loss_value_is_invalid),
}
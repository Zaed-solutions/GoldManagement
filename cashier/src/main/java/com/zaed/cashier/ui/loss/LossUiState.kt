package com.zaed.cashier.ui.loss

import androidx.annotation.StringRes
import com.google.type.DateTime
import com.zaed.common.R
import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.loss.DatedLoss
import com.zaed.common.data.model.loss.StoreLoss
import com.zaed.common.ui.util.DateFormat

data class LossUiState(
    val losses: List<StoreLoss> = emptyList(),
    val currentUser: User = User(),
    val isLoading: Boolean = false,
    @StringRes val errorMessage: Int? = null,
    val fieldError: LossFieldsError = LossFieldsError.NONE,
    val successMessage: String? = null,
    val datedLosses : List<DatedLoss> = emptyList(),
    val groupedByFilter: DateFormat = DateFormat.DATE
)

enum class LossFieldsError(@StringRes val message: Int) {
    NONE(0),
    LOSS_VALUE_IS_EMPTY(R.string.loss_value_is_empty),
    LOSS_REASON_IS_EMPTY(R.string.loss_reason_is_empty),
    LOSS_VALUE_IS_INVALID(R.string.loss_value_is_invalid),
}
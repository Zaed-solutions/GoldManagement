package com.zaed.cashier.ui.sales.details

import androidx.annotation.StringRes
import com.zaed.common.data.model.StoreSale

data class SaleDetailsUiState(
    val storeSale: StoreSale = StoreSale(),
    val isLoading: Boolean = false,
    val errorMessage: Exception? = null,
    val fieldError: SaleDetailsFieldsError = SaleDetailsFieldsError.NONE,
    val successMessage: String? = null
)

enum class SaleDetailsFieldsError(@StringRes val message: Int) {
    NONE(0),
    EMPTY_FIELD(1),
    INVALID_FIELD(2),
}
package com.zaed.common.ui.saledetails.cashiersaledetails

import androidx.annotation.StringRes
import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.sale.StoreSale

data class SaleDetailsUiState(
    val isLoading: Boolean = false,
    val isSaleDeleted: Boolean = false,
    val storeSale: StoreSale = StoreSale(),
    val errorMessage: Exception? = null,
    val fieldError: SaleDetailsFieldsError = SaleDetailsFieldsError.NONE,
    val successMessage: String? = null,
    val currentUser: User = User()
)

enum class SaleDetailsFieldsError(@StringRes val message: Int) {
    NONE(0),
    EMPTY_FIELD(1),
    INVALID_FIELD(2),
}
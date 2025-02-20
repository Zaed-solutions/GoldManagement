package com.zaed.cashier.ui.addsale

import com.zaed.common.data.model.Category
import com.zaed.common.data.model.sale.StoreSale
import com.zaed.common.data.model.authentication.User

data class AddSaleUiState(
    val categories: List<Category> = emptyList(),
    val currentUser: User = User(),
    val sale: StoreSale = StoreSale(),
    val isFinished: Boolean = false
)

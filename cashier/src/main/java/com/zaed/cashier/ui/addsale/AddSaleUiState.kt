package com.zaed.cashier.ui.addsale

import com.zaed.common.data.model.Category
import com.zaed.common.data.model.Product
import com.zaed.common.data.model.StoreSale
import com.zaed.common.data.model.User

data class AddSaleUiState(
    val categories: List<Category> = emptyList(),
    val currentUser: User = User(),
    val sale: StoreSale = StoreSale(),
    val isFinished: Boolean = false
)

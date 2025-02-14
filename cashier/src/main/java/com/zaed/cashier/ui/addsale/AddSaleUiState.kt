package com.zaed.cashier.ui.addsale

import com.zaed.common.data.model.Product
import com.zaed.common.data.model.StoreSale
import com.zaed.common.data.model.User

data class AddSaleUiState(
    val allProducts: List<Product> = emptyList(),
    val currentUser: User = User(),
    val sale: StoreSale = StoreSale(),
    val isFinished: Boolean = false
)

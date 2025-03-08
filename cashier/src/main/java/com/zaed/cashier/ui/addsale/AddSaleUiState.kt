package com.zaed.cashier.ui.addsale

import com.zaed.common.data.model.Category
import com.zaed.common.data.model.sale.StoreSale
import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.customer.WholeSaleCustomer

data class AddSaleUiState(
    val categories: List<Category> = emptyList(),
    val currentUser: User = User(),
    val selectedCustomer: WholeSaleCustomer = WholeSaleCustomer(),
    val sale: StoreSale = StoreSale(),
    val isFinished: Boolean = false,
    val isLoading: Boolean = false
)

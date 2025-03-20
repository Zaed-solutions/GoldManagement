package com.zaed.cashier.ui.addsale

import com.zaed.common.data.model.category.Category
import com.zaed.common.data.model.sale.StoreTransaction
import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.customer.WholeSaleCustomer

data class AddSaleUiState(
    val categories: List<Category> = emptyList(),
    val currentUser: User = User(),
    val selectedCustomer: WholeSaleCustomer = WholeSaleCustomer(),
    val sale: StoreTransaction = StoreTransaction(),
    val isFinished: Boolean = false,
    val isLoading: Boolean = false
)

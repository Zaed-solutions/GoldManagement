package com.zaed.distributor.ui.addproductsale

import com.zaed.common.data.model.Category
import com.zaed.common.data.model.User
import com.zaed.common.data.model.WholeSaleCustomer
import com.zaed.common.data.model.WholesaleProductSale

data class AddProductSaleUiState(
    val isFinished: Boolean = false,
    val sale: WholesaleProductSale = WholesaleProductSale(),
    val selectedCustomer: WholeSaleCustomer = WholeSaleCustomer(),
    val customerSearchQuery: String = "",
    val suggestedCustomers: List<WholeSaleCustomer> = emptyList(),
    val currentUser: User = User(),
    val categories: List<Category> = emptyList()
)

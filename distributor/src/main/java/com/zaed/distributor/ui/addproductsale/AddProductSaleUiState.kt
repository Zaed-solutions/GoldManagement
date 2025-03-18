package com.zaed.distributor.ui.addproductsale

import com.zaed.common.data.model.Category
import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.sale.WholesaleTransaction

data class AddProductSaleUiState(
    val isFinished: Boolean = false,
    val isLoading: Boolean = false,
    val initialSale: WholesaleTransaction = WholesaleTransaction(),
    val sale: WholesaleTransaction = WholesaleTransaction(),
    val selectedCustomer: WholeSaleCustomer = WholeSaleCustomer(),
    val customerSearchQuery: String = "",
    val suggestedCustomers: List<WholeSaleCustomer> = emptyList(),
    val currentUser: User = User(),
    val totalPaid: Double = 0.0,
    val payments: List<Payment> = emptyList(),
    val categories: List<Category> = emptyList()
)

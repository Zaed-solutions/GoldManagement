package com.zaed.common.ui.addGoldSale

import com.zaed.common.data.model.Category
import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.sale.WholesaleGoldSale

data class AddGoldSaleUiState(
    val isFinished: Boolean = false,
    val isLoading: Boolean = false,
    val initialSale: WholesaleGoldSale = WholesaleGoldSale(),
    val sale: WholesaleGoldSale = WholesaleGoldSale(),
    val selectedCustomer: WholeSaleCustomer = WholeSaleCustomer(),
    val customerSearchQuery: String = "",
    val suggestedCustomers: List<WholeSaleCustomer> = emptyList(),
    val currentUser: User = User(),
    val totalPaid: Double = 0.0,
    val payments: List<Payment> = emptyList(),
)

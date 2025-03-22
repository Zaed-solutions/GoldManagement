package com.zaed.distributor.ui.addproductsale

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.category.Category
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentType
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
    val payments: List<Payment> = emptyList(),
    val categories: List<Category> = emptyList()
){
    val totalPaid
        get() = payments.filter { it.type != PaymentType.FUTURES }
            .sumOf { if (it.type == PaymentType.REMAIN) it.amount.unaryMinus() else it.amount }
}

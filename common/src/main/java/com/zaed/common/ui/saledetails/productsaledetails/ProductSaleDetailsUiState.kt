package com.zaed.common.ui.saledetails.productsaledetails

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.sale.WholesaleProductTransaction

data class ProductSaleDetailsUiState(
    val isLoading: Boolean = false,
    val isSaleDeleted: Boolean = false,
    val sale: WholesaleProductTransaction = WholesaleProductTransaction(),
    val currentUser: User = User(),
    val customer: WholeSaleCustomer = WholeSaleCustomer(),
    val cashPayments: List<Payment> = emptyList()
)

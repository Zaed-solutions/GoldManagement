package com.zaed.distributor.ui.productsaledetails

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.MoneyPayment
import com.zaed.common.data.model.sale.WholesaleProductSale

data class ProductSaleDetailsUiState(
    val isLoading: Boolean = false,
    val isSaleDeleted: Boolean = false,
    val currentUser: User = User(),
    val customer: WholeSaleCustomer = WholeSaleCustomer(),
    val sale: WholesaleProductSale = WholesaleProductSale(),
    val moneyPayments: List<MoneyPayment> = emptyList()
)

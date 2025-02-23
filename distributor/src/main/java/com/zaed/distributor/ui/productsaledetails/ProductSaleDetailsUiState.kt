package com.zaed.distributor.ui.productsaledetails

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.payment.MoneyPayment
import com.zaed.common.data.model.sale.WholesaleProductSale

data class ProductSaleDetailsUiState(
    val isLoading: Boolean = false,
    val isSaleDeleted: Boolean = false,
    val currentUser: User = User(),
    val sale: WholesaleProductSale = WholesaleProductSale(),
    val moneyPayments: List<MoneyPayment> = emptyList()
)

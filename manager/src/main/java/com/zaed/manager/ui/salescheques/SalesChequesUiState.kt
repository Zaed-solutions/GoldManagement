package com.zaed.manager.ui.salescheques

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.sale.WholesaleSale

data class SalesChequesUiState(
    val loading: Boolean = false,
    val customer: WholeSaleCustomer = WholeSaleCustomer(),
    val currentDistributor: User = User(),
    val payments:  List<Payment> = emptyList(),
    val sales : List<WholesaleSale> = emptyList(),
)
package com.zaed.distributor.ui.customerdetails

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.MoneyPayment
import com.zaed.common.data.model.sale.WholesaleSale

data class CustomerDetailsUiState(
    val loading: Boolean = false,
    val customer: WholeSaleCustomer = WholeSaleCustomer(),
    val currentDistributor: User = User(),
    val payments: Map<String, List<MoneyPayment>> = emptyMap(),
    val paymentDirection : Boolean = false,
    val sales : List<WholesaleSale> = emptyList(),
    val currentMoneyPayment: MoneyPayment = MoneyPayment(),
    val tempMoneyPayment: MoneyPayment = MoneyPayment()
)
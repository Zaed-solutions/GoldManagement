package com.zaed.distributor.ui.customerdetails

import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.sale.WholesaleSale

data class CustomerDetailsUiState(
    val loading: Boolean = false,
    val customer: WholeSaleCustomer = WholeSaleCustomer(),
    val payments: Map<String, List<Payment>> = emptyMap(),
    val paymentDirection : Boolean = false,
    val sales : List<WholesaleSale> = emptyList(),
    val currentPayment: Payment = Payment(),
    val tempPayment: Payment = Payment()
)
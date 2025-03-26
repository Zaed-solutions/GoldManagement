package com.zaed.common.data.model.customer.request

data class FetchWholesaleCustomerSalesRequest (
    val customerId: String,
    val withOutStandingBill: Boolean = false,
)

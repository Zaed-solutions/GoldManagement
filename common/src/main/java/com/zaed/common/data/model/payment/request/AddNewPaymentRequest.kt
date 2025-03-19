package com.zaed.common.data.model.payment.request

import com.zaed.common.data.model.payment.Payment

data class AddNewPaymentRequest(
    val isSupplier: Boolean = false,
    val customerId: String,
    val payment: Payment
)
package com.zaed.common.data.model.payment.request

data class DeletePaymentRequest(
    val customerId: String,
    val paymentId: String,
    val amount: Double
)
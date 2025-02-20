package com.zaed.common.data.model.payment.request

import com.zaed.common.data.model.payment.Payment

data class EditPaymentRequest(
    val payment: Payment,
    val customerId: String,
)

package com.zaed.common.data.model.request

import com.zaed.common.data.model.Payment

data class AddNewPaymentRequest(
    val customerId: String,
    val payment: Payment
)
package com.zaed.common.data.model.payment.request

import com.zaed.common.data.model.payment.MoneyPayment

data class AddNewPaymentRequest(
    val customerId: String,
    val moneyPayment: MoneyPayment
)
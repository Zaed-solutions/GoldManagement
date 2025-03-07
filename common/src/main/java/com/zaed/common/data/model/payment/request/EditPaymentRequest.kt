package com.zaed.common.data.model.payment.request

import com.zaed.common.data.model.payment.Payment

data class EditPaymentRequest(
    val newCashPayment: Payment,
    val oldAmount: Double,
    val customerId: String,
) {
    val diff: Double
        get() = newCashPayment.amount - oldAmount
}

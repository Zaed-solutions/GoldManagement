package com.zaed.common.data.model.payment.request

import com.zaed.common.data.model.payment.Payment

data class EditPaymentRequest(
    val newPayment: Payment,
    val oldAmount: Double,
    val customerId: String,
) {
    val diff: Double
        get() = newPayment.amount - oldAmount
}

package com.zaed.common.data.model.payment.request

import com.zaed.common.data.model.payment.MoneyPayment

data class EditPaymentRequest(
    val newMoneyPayment: MoneyPayment,
    val oldAmount: Double,
    val customerId: String,
) {
    val diff: Double
        get() = newMoneyPayment.amount - oldAmount
}

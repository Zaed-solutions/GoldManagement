package com.zaed.common.data.model.payment.request

import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.signedAmount

data class EditPaymentRequest(
    val isSupplier: Boolean = false,
    val newPayment: Payment,
    val oldPayment: Payment,
    val customerId: String,
) {
    val diff: Double
        get() = newPayment.signedAmount() - oldPayment.signedAmount()
}

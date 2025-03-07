package com.zaed.common.data.model.payment.request

import com.zaed.common.data.model.payment.PaymentType

data class DeletePaymentRequest(
    val customerId: String,
    val paymentId: String,
    val amount: Double,
    val employeeId: String,
    val employeeName: String,
    val type : PaymentType
)
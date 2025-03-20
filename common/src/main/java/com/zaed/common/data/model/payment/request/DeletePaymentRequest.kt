package com.zaed.common.data.model.payment.request

import com.zaed.common.data.model.payment.Payment

data class DeletePaymentRequest(
    val isSupplier: Boolean = false,
    val payment: Payment,
    val employeeId: String,
    val employeeName: String,

)
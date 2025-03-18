package com.zaed.common.data.model.sale.request

import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.sale.WholesaleTransaction

data class UpdatePurchaseRequest (
    val purchase: WholesaleTransaction,
    val payments: List<Payment>,
    val employeeId: String,
    val employeeName: String,
)

data class UpdateWholesaleRequest (
    val sale: WholesaleTransaction,
    val payments: List<Payment>,
    val employeeId: String,
    val employeeName: String,
    val isAdmin: Boolean = false
)

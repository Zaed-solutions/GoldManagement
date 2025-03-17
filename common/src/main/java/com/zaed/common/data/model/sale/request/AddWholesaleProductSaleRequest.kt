package com.zaed.common.data.model.sale.request

import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.sale.WholesaleTransaction
data class AddPurchaseRequest(
    val purchase: WholesaleTransaction,
    val payments: List<Payment>
)


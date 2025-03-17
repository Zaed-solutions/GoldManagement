package com.zaed.common.data.model.sale.request

import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.sale.WholesaleTransaction

data class AddWholesaleRequest(
    val sale: WholesaleTransaction,
    val payments: List<Payment>,
    val isAdmin: Boolean = false
)

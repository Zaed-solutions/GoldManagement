package com.zaed.common.data.model.sale.request

import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.sale.WholesaleGoldTransaction

data class AddWholesaleGoldSaleRequest(
    val sale: WholesaleGoldTransaction,
    val payments: List<Payment>,
    val isAdmin: Boolean = false
)

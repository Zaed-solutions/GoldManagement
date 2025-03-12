package com.zaed.common.data.model.sale.request

import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.sale.WholesaleGoldSale

data class AddWholesaleGoldSaleRequest(
    val sale: WholesaleGoldSale,
    val payments: List<Payment>,
    val isAdmin: Boolean = false
)

package com.zaed.common.data.model.sale.request

import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.sale.WholesaleProductSale

data class AddWholesaleProductSaleRequest(
    val sale: WholesaleProductSale,
    val payments: List<Payment>
)

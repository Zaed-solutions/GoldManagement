package com.zaed.common.data.model.sale.request

import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.purchase.Purchase
import com.zaed.common.data.model.sale.WholesaleProductTransaction

data class AddWholesaleProductSaleRequest(
    val sale: WholesaleProductTransaction,
    val payments: List<Payment>
)

data class AddPurchaseRequest(
    val purchase: Purchase,
    val payments: List<Payment>
)


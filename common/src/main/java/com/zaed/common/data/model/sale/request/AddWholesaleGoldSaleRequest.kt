package com.zaed.common.data.model.sale.request

import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.data.model.payment.CashPayment
import com.zaed.common.data.model.sale.WholesaleGoldSale

data class AddWholesaleGoldSaleRequest (
    val sale: WholesaleGoldSale,
    val cashPayments: List<CashPayment>,
    val goldPayments: List<GoldPayment>

    )

package com.zaed.common.data.model.sale.request

import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.data.model.payment.MoneyPayment
import com.zaed.common.data.model.sale.WholesaleGoldSale

data class AddWholesaleGoldSaleRequest (
    val sale: WholesaleGoldSale,
    val moneyPayments: List<MoneyPayment>,
    val goldPayments: List<GoldPayment>

    )

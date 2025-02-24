package com.zaed.common.data.model.sale.request

import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.data.model.payment.MoneyPayment
import com.zaed.common.data.model.sale.WholesaleGoldSale
import com.zaed.common.data.model.sale.WholesaleProductSale

data class UpdateWholesaleProductSaleRequest (
    val sale: WholesaleProductSale,
    val moneyPayments: List<MoneyPayment>,
    val employeeId: String,
    val employeeName: String,
)

data class UpdateWholesaleGoldSaleRequest (
    val sale: WholesaleGoldSale,
    val moneyPayments: List<MoneyPayment>,
    val goldPayments: List<GoldPayment>,
    val employeeId: String,
    val employeeName: String,
)

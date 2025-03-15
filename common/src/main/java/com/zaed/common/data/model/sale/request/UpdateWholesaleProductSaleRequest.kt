package com.zaed.common.data.model.sale.request

import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.purchase.Purchase
import com.zaed.common.data.model.sale.WholesaleGoldTransaction
import com.zaed.common.data.model.sale.WholesaleProductTransaction

data class UpdateWholesaleProductSaleRequest (
    val sale: WholesaleProductTransaction,
    val payments: List<Payment>,
    val employeeId: String,
    val employeeName: String,
)

data class UpdatePurchaseRequest (
    val purchase: Purchase,
    val payments: List<Payment>,
    val employeeId: String,
    val employeeName: String,
)

data class UpdateWholesaleGoldSaleRequest (
    val sale: WholesaleGoldTransaction,
    val payments: List<Payment>,
    val employeeId: String,
    val employeeName: String,
    val isAdmin: Boolean = false
)

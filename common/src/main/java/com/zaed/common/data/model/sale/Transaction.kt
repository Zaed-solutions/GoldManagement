package com.zaed.common.data.model.sale

import com.zaed.common.data.model.authentication.ChangeLog
import java.util.Date

abstract class Transaction(
    open val id: String,
    open val accountId: String,
    open val customerName: String,
    open val customerPhone: String,
    open val createdAt: Date,
    open val receiptNumber: String,
    open val logs: List<ChangeLog>,
    open  val deleted: Boolean,
    open  val totalAmount: Double,
    open val products: List<Product>,
    open val outStandingBill : Boolean = false,
){
    open val profit
        get() = products.sumOf { it.totalPriceAfterDiscount } - products.sumOf { it.buyingPrice * it.quantity * it.grams }
}
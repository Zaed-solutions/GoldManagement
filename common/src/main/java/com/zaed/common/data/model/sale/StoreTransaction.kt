package com.zaed.common.data.model.sale

import com.zaed.common.data.model.authentication.ChangeLog
import kotlinx.serialization.Transient
import java.util.Date

data class StoreTransaction(
    override val id: String = "",
    override val createdAt: Date = Date(),
    val storeId: String = "",
    val storeName: String = "",
    val storeLocation: String = "",
    val employeeName: String = "",
    val employeeId: String = "",
    override val customerName: String = "",
    override val receiptNumber: String = "",
    val customerEmail: String = "",
    override val products: List<Product> = emptyList(),
    val discount: Discount = Discount(),
    override val deleted: Boolean = false,
    override val logs: List<ChangeLog> = emptyList(),
    override val accountId: String="",
    override val customerPhone: String=""
): Transaction(
    id = id,
    accountId = accountId,
    customerName = customerName,
    customerPhone = customerPhone,
    createdAt = createdAt,
    receiptNumber = receiptNumber,
    logs = logs,
    deleted = deleted,
    products = products,
    totalAmount = 0.0,
) {
    @Transient
    override val profit
        get() = products.sumOf { it.totalPriceAfterDiscount } - products.sumOf { it.buyingPrice * it.quantity * it.grams }
    @Transient
    override val totalAmount
        get() = products.sumOf { it.totalPriceAfterDiscount }
    @Transient
    val totalAmountBeforeDiscount
        get() = products.sumOf { it.totalPriceBeforeDiscount }
    @Transient
    val totalDiscount
        get() = totalAmountBeforeDiscount - totalAmount
}

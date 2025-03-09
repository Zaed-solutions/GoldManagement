package com.zaed.common.data.model.sale

import com.zaed.common.data.model.authentication.ChangeLog
import kotlinx.serialization.Transient
import java.util.Date

data class StoreSale(
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
    override val customerId: String="",
    override val customerPhone: String=""
): Sale() {
    @Transient
    override val totalAmount
        get() = products.sumOf { it.totalPriceAfterDiscount }
}

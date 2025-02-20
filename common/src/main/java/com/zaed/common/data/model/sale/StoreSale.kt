package com.zaed.common.data.model.sale

import com.zaed.common.data.model.authentication.ChangeLog
import kotlinx.serialization.Transient
import java.util.Date

data class StoreSale(
    val id: String = "",
    val createdAt: Date = Date(),
    val storeId: String = "",
    val storeName: String = "",
    val employeeName: String = "",
    val employeeId: String = "",
    val customerName: String = "",
    val receiptNumber: String = "",
    val customerPhoneNumber: String = "",
    val customerEmail: String = "",
    val products: List<Product> = emptyList(),
    val discount: Discount = Discount(),
    val deleted: Boolean = false,
    val logs: List<ChangeLog> = emptyList()
) {
    @Transient
    val totalPrice
        get() = products.sumOf { it.gramPrice * it.grams } - when (discount.type) {
            DiscountType.NONE -> 0.0
            DiscountType.PERCENTAGE -> products.sumOf { it.grams * it.gramPrice } * (discount.value / 100.0)
            DiscountType.AMOUNT -> discount.value
        }
}

package com.zaed.common.data.model

import kotlinx.serialization.Serializable
import java.util.Date

data class StoreSale(
    val id: String = "",
    val createdAt: Date = Date(),
    val storeId: String = "",
    val storeName: String = "",
    val employeeName: String = "",
    val employeeId: String = "",
    val customerName: String = "",
    val customerPhoneNumber: String = "",
    val customerEmail: String = "",
    val products: List<Product> = emptyList(),
    val discount: Discount = Discount(),
) {
    val totalPrice
        get() = products.sumOf { it.gramPrice * it.grams } - when(discount.type){
        DiscountType.NONE -> 0.0
        DiscountType.PERCENTAGE -> products.sumOf { it.grams * it.gramPrice } * (discount.value/100.0)
        DiscountType.AMOUNT -> discount.value
    }
}

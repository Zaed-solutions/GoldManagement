package com.zaed.common.data.model

data class StoreSale(
    val id: String = "",
    val createdAt: Long = 0,
    val employeeName: String = "",
    val employeeId: String = "",
    val customerName: String = "",
    val customerPhoneNumber: String = "",
    val customerEmail: String = "",
    val products: List<Product> = emptyList(),
    val discount: Discount = Discount(),
){
    val totalPrice = products.sumOf { it.price } - when(discount.type){
        DiscountType.NONE -> 0.0
        DiscountType.PERCENTAGE -> products.sumOf { it.price } * discount.value
        DiscountType.AMOUNT -> discount.value
    }
}

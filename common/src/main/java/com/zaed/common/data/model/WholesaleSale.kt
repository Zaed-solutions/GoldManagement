package com.zaed.common.data.model

import java.util.Date

data class WholesaleSale(
    val id: String = "",
    val customerId: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val paymentId: String = "",
    val paid: Boolean = false,
    val distributorId: String = "",
    val distributorName: String= "",
    val createdAt: Date = Date(),
    val logs: List<ChangeLog> = emptyList(),
    val deleted: Boolean = false,
    val products: List<Product>,
)

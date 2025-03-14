package com.zaed.common.data.model.purchase

import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.sale.Product
import java.util.Date

data class Purchase(
    val id: String = "",
    val supplierId: String = "",
    val supplierName: String = "",
    val supplierPhone: String = "",
    val createdAt: Date = Date(),
    val receiptNumber: String = "",
    val logs: List<ChangeLog> = emptyList(),
    val deleted: Boolean = false,
    val totalAmount: Double = 0.0,
    val products: List<Product> = emptyList()
)
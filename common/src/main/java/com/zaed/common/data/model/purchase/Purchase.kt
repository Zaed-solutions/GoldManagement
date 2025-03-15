package com.zaed.common.data.model.purchase

import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.payment.PaymentStatus
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.Transaction
import kotlinx.serialization.Transient
import java.util.Date

data class Purchase(
    override val id: String = "",
    val supplierId: String = "",
    val supplierName: String = "",
    val supplierPhone: String = "",
    override val createdAt: Date = Date(),
    override val receiptNumber: String = "",
    override val logs: List<ChangeLog> = emptyList(),
    override val deleted: Boolean = false,
    override val products: List<Product> = emptyList(),
    val paymentsIds: List<String> = emptyList(),
    val paymentStatus: PaymentStatus = PaymentStatus.UNPAID,
    val distributorId: String = "",
    val distributorName: String = ""
) : Transaction(
    id = id,
    customerId = supplierId,
    customerName = supplierName,
    customerPhone = supplierPhone,
    createdAt = createdAt,
    receiptNumber = receiptNumber,
    logs = logs,
    deleted = deleted,
    totalAmount = products.sumOf { it.grams * it.gramPrice * it.quantity } - products.sumOf { it.discountAmount },
    products = products
){
    @Transient
    val totalPriceBeforeDiscount
        get() = products.sumOf { it.grams * it.gramPrice * it.quantity }
    override val totalAmount
        get() = totalPriceBeforeDiscount - products.sumOf { it.discountAmount }
}
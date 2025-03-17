package com.zaed.common.data.model.sale

import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.payment.PaymentStatus
import kotlinx.serialization.Transient
import java.util.Date

data class WholesaleProductTransaction(
    override val id: String = "",
    override val customerId: String = "",
    override val customerName: String = "",
    override val customerPhone: String = "",
    override val distributorId: String = "",
    override val distributorName: String = "",
    override val createdAt: Date = Date(),
    override val logs: List<ChangeLog> = emptyList(),
    override val deleted: Boolean = false,
    val receiptStatus: ReceiptStatus = ReceiptStatus.NOT_REQUESTED,
    override val products: List<Product> = emptyList(),
    val paymentsIds: List<String> = emptyList(),
    override val paymentStatus: PaymentStatus = PaymentStatus.UNPAID,
    override val receiptNumber: String = "",
    override val isSale: Boolean = true
) : WholesaleTransaction(
    id = id,
    customerId = customerId,
    customerName = customerName,
    customerPhone = customerPhone,
    createdAt = createdAt,
    logs = logs,
    deleted = deleted,
    paymentStatus = paymentStatus,
    receiptNumber = receiptNumber,
    totalAmount = products.sumOf { it.grams * it.gramPrice * it.quantity } - products.sumOf { it.discountAmount },
    distributorId = distributorId,
    distributorName = distributorName,
    products = products,
    isSale = isSale
) {
    @Transient
    val totalPriceBeforeDiscount
        get() = products.sumOf { it.totalPriceBeforeDiscount }
    override val totalAmount
        get() = products.sumOf { it.totalPriceAfterDiscount }
}
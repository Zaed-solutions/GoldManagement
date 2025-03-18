package com.zaed.common.data.model.sale

import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.payment.PaymentStatus
import com.zaed.common.ui.addpurchase.ProductType
import kotlinx.serialization.Transient
import java.util.Date

data class WholesaleTransaction(
    override val id: String = "",
    override val customerId: String = "",
    override val customerName: String = "",
    override val customerPhone: String = "",
    val distributorId: String = "",
    val distributorName: String = "",
    override val createdAt: Date = Date(),
    override val logs: List<ChangeLog> = emptyList(),
    override val deleted: Boolean = false,
    val receiptStatus: ReceiptStatus = ReceiptStatus.NOT_REQUESTED,
    override val products: List<Product> = emptyList(),
    val paymentsIds: List<String> = emptyList(),
    val paymentStatus: PaymentStatus = PaymentStatus.UNPAID,
    override val receiptNumber: String = "",
    val productType: ProductType = ProductType.PRODUCT,
    val sale : Boolean = true
) : Transaction(
    id = id,
    customerId = customerId,
    customerName = customerName,
    customerPhone = customerPhone,
    createdAt = createdAt,
    receiptNumber = receiptNumber,
    logs = logs,
    deleted = deleted,
    totalAmount = products.sumOf { it.totalPriceAfterDiscount },
    products = products
){
    @Transient
    val totalPriceBeforeDiscount
        get() = products.sumOf { it.totalPriceBeforeDiscount }
    override val totalAmount
        get() = products.sumOf { it.totalPriceAfterDiscount }
}



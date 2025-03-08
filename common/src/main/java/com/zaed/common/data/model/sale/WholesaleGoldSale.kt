package com.zaed.common.data.model.sale

import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.payment.PaymentStatus
import java.util.Date

data class WholesaleGoldSale(
    override val id: String = "",
    override val customerId: String = "",
    override val customerName: String = "",
    override val customerPhone: String = "",
    override val distributorId: String = "",
    override val distributorName: String = "",
    override val receiptNumber: String ="",
    val paymentsIds: List<String> = emptyList(),
    override val products :List<Product> = emptyList(),
    val receiptStatus: ReceiptStatus = ReceiptStatus.NOT_REQUESTED,
    override val createdAt: Date = Date(),
    override val logs: List<ChangeLog> = emptyList(),
    override val deleted: Boolean = false,
    override val paymentStatus: PaymentStatus = PaymentStatus.UNPAID,
): WholesaleSale(
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
    products = products
){
    override val totalAmount
        get() = products.sumOf { (it.grams * it.gramPrice * it.quantity)+it.laborCost/*TODO*/ } - products.sumOf { it.discountAmount }
}

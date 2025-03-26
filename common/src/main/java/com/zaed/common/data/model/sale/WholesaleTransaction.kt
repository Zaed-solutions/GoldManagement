package com.zaed.common.data.model.sale

import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.payment.PaymentStatus
import com.zaed.common.ui.addpurchase.ProductType
import kotlinx.serialization.Transient
import java.util.Date

data class WholesaleTransaction(
    override val id: String = "",
    override val accountId: String = "",
    override val customerName: String = "",
    override val customerPhone: String = "",
    val distributorId: String = "",
    val distributorName: String = "",
    val discount: Double = 0.0,
    override val createdAt: Date = Date(),
    override val logs: List<ChangeLog> = emptyList(),
    override val deleted: Boolean = false,
    val receiptStatus: ReceiptStatus = ReceiptStatus.NOT_REQUESTED,
    override val products: List<Product> = emptyList(),
    val paymentsIds: List<String> = emptyList(),
    val paymentStatus: PaymentStatus = PaymentStatus.UNPAID,
    override val receiptNumber: String = "",
    val productType: ProductType = ProductType.PRODUCT,
    val sale: Boolean = true,
    override val outStandingBill : Boolean = false
) : Transaction(
    id = id,
    accountId = accountId,
    customerName = customerName,
    customerPhone = customerPhone,
    createdAt = createdAt,
    receiptNumber = receiptNumber,
    logs = logs,
    deleted = deleted,
    totalAmount = products.sumOf { it.totalPriceAfterDiscount },
    products = products,
    outStandingBill = outStandingBill
) {
    @Transient
    override val profit
        get() = products.sumOf { it.totalPriceAfterDiscount } - products.sumOf { it.buyingPrice * it.quantity * it.grams }

    @Transient
    val totalPriceBeforeDiscount
        get() = products.sumOf { it.totalPriceBeforeDiscount }
    override val totalAmount
        get() = totalPriceBeforeDiscount - discount
}



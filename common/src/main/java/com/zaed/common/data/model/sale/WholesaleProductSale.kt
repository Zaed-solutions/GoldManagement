package com.zaed.common.data.model.sale

import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.payment.PaymentStatus
import kotlinx.serialization.Transient
import java.util.Date

data class WholesaleProductSale(
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
    val products: List<Product> = emptyList(),
    val paymentsIds: List<String> = emptyList(),
    override val paymentStatus: PaymentStatus = PaymentStatus.UNPAID,
    override val receiptNumber: String = "",
) : WholesaleSale(){
    @Transient
    val totalPriceBeforeDiscount
        get() = products.sumOf { it.grams*it.gramPrice *it.quantity}
    val totalPriceAfterDiscount
        get() = totalPriceBeforeDiscount - products.sumOf { it.discountAmount }
}
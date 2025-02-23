package com.zaed.common.data.model.sale

import com.zaed.common.data.model.authentication.ChangeLog
import java.util.Date

data class WholesaleGoldSale(
    override val id: String = "",
    override val customerId: String = "",
    override val customerName: String = "",
    override val customerPhone: String = "",
    override val distributorId: String = "",
    override val distributorName: String = "",
    override val receiptNumber: String ="",

    val gramsAmount: Double = 0.0,
    val pricePerGram: Double = 0.0,
    val totalPrice: Double = 0.0,
    val laborCost: Double = 0.0,
    val receiptStatus: ReceiptStatus = ReceiptStatus.NOT_REQUESTED,

    override val createdAt: Date = Date(),
    override val logs: List<ChangeLog> = emptyList(),
    override val deleted: Boolean = false,
    override val paid: Boolean = false
): WholesaleSale()

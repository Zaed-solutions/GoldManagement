package com.zaed.common.data.model.sale

import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.payment.PaymentStatus
import java.util.Date

open class WholesaleSale(
    override val id: String,
    override val customerId: String,
    override val customerName: String,
    override val customerPhone: String,
    override val createdAt: Date,
    override val receiptNumber: String,
    override val logs: List<ChangeLog>,
    override val deleted: Boolean,
    override val totalAmount: Double,
    override val products: List<Product>,
    open val paymentStatus: PaymentStatus,
    open val distributorId: String,
    open val distributorName: String
) : Sale()



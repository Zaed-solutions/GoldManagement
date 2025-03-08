package com.zaed.common.data.model.sale

import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.payment.PaymentStatus
import java.util.Date

abstract class Sale {
    abstract val id: String
    abstract val customerId: String
    abstract val customerName: String
    abstract val customerPhone: String
    abstract val createdAt: Date
    abstract val receiptNumber: String
    abstract val logs: List<ChangeLog>
    abstract val deleted: Boolean
    abstract val totalAmount: Double
    abstract val products: List<Product>
}
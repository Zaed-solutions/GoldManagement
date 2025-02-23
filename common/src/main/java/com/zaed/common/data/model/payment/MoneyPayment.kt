package com.zaed.common.data.model.payment

import com.zaed.common.data.model.authentication.ChangeLog
import java.util.Date

// طريقة الدفع النقدي
data class MoneyPayment(
    override val id: String = "",
    override val customerId: String = "",
    override val type: PaymentType = PaymentType.CASH,
    val amount: Double = 0.0, // خاص بـ MoneyPayment
    override val deleted: Boolean = false,
    override val receiptNumber: String = "",
    override val createdAt: Date = Date(),
    override val logs: List<ChangeLog> = emptyList()
) : Payment(id, customerId, type, deleted, receiptNumber, createdAt, logs)


package com.zaed.common.data.model.payment

import com.zaed.common.data.model.authentication.ChangeLog
import java.util.Date

data class FuturePayment(
    override var id: String = "",
    override var customerId: String = "",
    override val type: PaymentType = PaymentType.FUTURES,
    override var amount: Double = 0.0,
    override val deleted: Boolean = false,
    override var given :Boolean = false,
    override var receiptNumber: String = "",
    override val createdAt: Date = Date(),
    override val logs: List<ChangeLog> = emptyList()
) : Payment(id, customerId, type,amount, deleted,given, receiptNumber, createdAt, logs)
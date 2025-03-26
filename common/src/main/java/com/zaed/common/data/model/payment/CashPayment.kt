package com.zaed.common.data.model.payment

import com.zaed.common.data.model.authentication.ChangeLog
import java.util.Date

data class CashPayment(
    override var id: String = "",
    override var accountId: String = "",
    override val type: PaymentType = PaymentType.CASH,
    override var amount: Double = 0.0,
    override var given :Boolean = false,
    override val deleted: Boolean = false,
    override var receiptNumber: String = "",
    override val createdAt: Date = Date(),
    override val logs: List<ChangeLog> = emptyList()
) : Payment(id, accountId, type,amount, deleted,given, receiptNumber, createdAt, logs)




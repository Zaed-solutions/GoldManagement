package com.zaed.common.data.model.payment

import com.zaed.common.data.model.authentication.ChangeLog
import java.util.Date

data class LossPayment(
    override var id: String = "",
    override var accountId: String = "",
    override val type: PaymentType = PaymentType.LOSS,
    override var amount: Double = 0.0,
    override val deleted: Boolean = false,
    override var receiptNumber: String = "",
    override var given :Boolean = false,
    override val createdAt: Date = Date(),
    override val logs: List<ChangeLog> = emptyList()
) : Payment(id, accountId, type,amount, deleted,given, receiptNumber, createdAt, logs)
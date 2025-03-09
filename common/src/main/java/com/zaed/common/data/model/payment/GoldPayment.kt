package com.zaed.common.data.model.payment

import com.zaed.common.data.model.authentication.ChangeLog
import java.util.Date

data class GoldPayment(
    override var id: String = "",
    override val customerId: String = "",
    override val type: PaymentType = PaymentType.GOLD,
    override var amount: Double = 0.0,
    val givenGoldAmount: Double = 0.0,
    val pricePerGram: Double = 0.0,
    val givenGoldKarat: Int = 0,
    override val deleted: Boolean = false,
    override var given :Boolean = false,
    override var receiptNumber: String = "",
    override val createdAt: Date = Date(),
    override val logs: List<ChangeLog> = emptyList()
) : Payment(id, customerId, type,amount = (givenGoldAmount * pricePerGram), deleted,given, receiptNumber, createdAt, logs)
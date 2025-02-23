package com.zaed.common.data.model.payment

import com.zaed.common.data.model.authentication.ChangeLog
import java.util.Date

data class GoldPayment(
    override val id: String = "",
    override val customerId: String = "",
    override val type: PaymentType = PaymentType.GOLD,
    val givenGoldAmount: Double = 0.0, // خاص بـ GoldPayment
    val givenGoldKarat: Int = 0,       // خاص بـ GoldPayment
    override val deleted: Boolean = false,
    override val receiptNumber: String = "",
    override val createdAt: Date = Date(),
    override val logs: List<ChangeLog> = emptyList()
) : Payment(id, customerId, type, deleted, receiptNumber, createdAt, logs)
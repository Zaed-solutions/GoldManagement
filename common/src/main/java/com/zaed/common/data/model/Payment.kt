package com.zaed.common.data.model

import java.util.Date

data class Payment(
    val id :String ="",
    val customerId: String = "",
    val type: PaymentType = PaymentType.CASH,
    val amount: Double = 0.0,
    val deleted: Boolean = false,
    val receiptNumber :String = "",
    val createdAt: Date = Date(),
    val logs :List <ChangeLog> = emptyList()
)


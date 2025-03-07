package com.zaed.common.data.model.payment

import com.zaed.common.data.model.authentication.ChangeLog
import java.util.Date

abstract class Payment(
    open val id: String = "",
    open val customerId: String = "",
    open val type: PaymentType,
    open val amount: Double = 0.0,
    open val deleted: Boolean = false,
    open val receiptNumber: String = "",
    open val createdAt: Date = Date(),
    open val logs: List<ChangeLog> = emptyList()
)
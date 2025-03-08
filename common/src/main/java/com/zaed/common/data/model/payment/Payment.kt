package com.zaed.common.data.model.payment

import com.zaed.common.data.model.authentication.ChangeLog
import java.util.Date

abstract class Payment(
    open var id: String = "",
    open val customerId: String = "",
    open val type: PaymentType,
    open var amount: Double = 0.0,
    open val deleted: Boolean = false,
    open var receiptNumber: String = "",
    open val createdAt: Date = Date(),
    open val logs: List<ChangeLog> = emptyList()
)
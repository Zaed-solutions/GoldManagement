package com.zaed.common.data.model.payment

import com.zaed.common.data.model.authentication.ChangeLog
import java.util.Date

open class Payment(
    open var id: String = "",
    open var customerId: String = "",
    open val type: PaymentType,
    open var amount: Double = 0.0,
    open var given: Boolean = false,
    open val deleted: Boolean = false,
    open var receiptNumber: String = "",
    open val createdAt: Date = Date(),
    open val logs: List<ChangeLog> = emptyList()
)
fun Payment.signedAmount(): Double =
    when (given) {
        true -> amount.unaryMinus()
        false -> amount
    }

package com.zaed.common.data.model.payment

import com.zaed.common.data.model.authentication.ChangeLog
import java.util.Date

data class BankTransferPayment(
    override var id: String = "",
    val bankName : String ="",
    val accountNumber : String ="",
    val accountHolderName : String ="",
    override var amount: Double = 0.0,
    override val customerId: String = "",
    override val type: PaymentType = PaymentType.BANK_TRANSFER,
    override val deleted: Boolean = false,
    override var receiptNumber: String = "",
    override val createdAt: Date = Date(),
    override val logs: List<ChangeLog> = emptyList()
) : Payment(id, customerId, type,amount, deleted, receiptNumber, createdAt, logs)
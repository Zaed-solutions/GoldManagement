package com.zaed.common.data.model.payment

import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.cheque.ChequeStatus
import java.util.Date

data class ChequePayment(
    override var id: String = "",
    val senderName : String ="",
    val paymentDate : Date = Date(),
    val city : String = "",
    val receiverName : String = "",
    val chequeFor : String = "",
    val cashed : Boolean = false,
    val chequeStatus : ChequeStatus = ChequeStatus.RECEIVED,
    val notes : String = "",
    val customerName : String = "",
    override var given :Boolean = false,
    override var customerId: String = "",
    override val type: PaymentType = PaymentType.CHEQUE,
    override var amount: Double = 0.0,
    override val deleted: Boolean = false,
    override var receiptNumber: String = "",
    override val createdAt: Date = Date(),

    override val logs: List<ChangeLog> = emptyList()
) : Payment(id, customerId, type,amount, deleted,given, receiptNumber, createdAt, logs)

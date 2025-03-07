package com.zaed.common.data.model.payment

import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.cheque.ChequeStatus
import java.util.Date

data class ChequePayment(
    override val id: String = "",
    val senderName : String ="",
    val paymentDate : Date = Date(),
    val city : String = "",
    val receiverName : String = "",
    val chequeFor : String = "",
    val chequeStatus : ChequeStatus = ChequeStatus.RECEIVED,
    val notes : String = "",
    override val customerId: String = "",
    override val type: PaymentType = PaymentType.CHEQUE,
    override val amount: Double = 0.0,
    override val deleted: Boolean = false,
    override val receiptNumber: String = "",
    override val createdAt: Date = Date(),
    override val logs: List<ChangeLog> = emptyList()
) : Payment(id, customerId, type,amount, deleted, receiptNumber, createdAt, logs)
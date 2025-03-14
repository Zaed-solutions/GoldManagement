package com.zaed.common.data.model.cheque

import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentType
import java.util.Date

data class ManagerCheque(
    override var id : String = "",
    val chequeType : ChequeType = ChequeType.PERSONAL,
    val chequeNumber : String = "",
    val receiverName : String = "",
    val chequeStatus : ChequeStatus = ChequeStatus.RECEIVED,
    val notes : String = "",
    override var given :Boolean = false,
    override val customerId: String = "",
    override val type: PaymentType = PaymentType.MANAGER_CHEQUES,
    override var amount: Double = 0.0,
    override val deleted: Boolean = false,
    override var receiptNumber: String = "",
    override val createdAt: Date = Date(),
    override val logs: List<ChangeLog> = emptyList()
):Payment( id, customerId, type,amount, deleted,given, receiptNumber, createdAt, logs)

fun ManagerCheque.toMap(): Map<String, Any> =
    mapOf(
        "id" to id,
        "chequeType" to chequeType,
        "chequeNumber" to chequeNumber,
        "amount" to amount,
        "receiverName" to receiverName,
        "chequeStatus" to chequeStatus,
        "notes" to notes,
    )
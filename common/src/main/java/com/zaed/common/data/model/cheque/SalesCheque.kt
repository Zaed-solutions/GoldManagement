package com.zaed.common.data.model.cheque

import java.util.Date

data class SalesCheque(
    val id : String = "",
    val senderName : String ="",
    val paymentDate : Date = Date(),
    val city : String = "",
    val receiverName : String = "",
    val amount : Double = 0.0,
    val chequeFor : String = "",
    val chequeStatus : ChequeStatus = ChequeStatus.RECEIVED,
    val notes : String = "",
)


fun SalesCheque.toMap(): Map<String, Any> =
    mapOf(
        "id" to id,
        "senderName" to senderName,
        "paymentDate" to paymentDate,
        "city" to city,
        "receiverName" to receiverName,
        "amount" to amount,
        "chequeFor" to chequeFor,
        "chequeStatus" to chequeStatus,
        "notes" to notes,
    )


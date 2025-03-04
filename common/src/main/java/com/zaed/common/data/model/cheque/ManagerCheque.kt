package com.zaed.common.data.model.cheque

import java.util.Date

data class ManagerCheque(
    val id : String = "",
    val chequeType : ChequeType = ChequeType.PERSONAL,
    val date : Date = Date(),
    val chequeNumber : String = "",
    val amount : Double = 0.0,
    val receiverName : String = "",
    val chequeStatus : ChequeStatus = ChequeStatus.RECEIVED,
    val notes : String = "",
)

fun ManagerCheque.toMap(): Map<String, Any> =
    mapOf(
        "id" to id,
        "chequeType" to chequeType,
        "date" to date,
        "chequeNumber" to chequeNumber,
        "amount" to amount,
        "receiverName" to receiverName,
        "chequeStatus" to chequeStatus,
        "notes" to notes,
    )
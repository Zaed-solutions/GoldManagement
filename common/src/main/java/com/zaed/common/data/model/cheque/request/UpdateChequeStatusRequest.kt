package com.zaed.common.data.model.cheque.request

import com.zaed.common.data.model.cheque.ChequeStatus

data class UpdateChequeStatusRequest(
    val chequeId :String ,
    val status : ChequeStatus ,
)
package com.zaed.common.data.model.cheque.request

import com.zaed.common.data.model.payment.ChequePayment

data class AddNewSalesChequeRequest(
    val chequePayment: ChequePayment = ChequePayment(),
)

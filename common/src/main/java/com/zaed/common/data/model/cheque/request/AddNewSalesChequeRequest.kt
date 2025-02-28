package com.zaed.common.data.model.cheque.request

import com.zaed.common.data.model.cheque.SalesCheque

data class AddNewSalesChequeRequest(
    val salesCheque: SalesCheque = SalesCheque(),
)

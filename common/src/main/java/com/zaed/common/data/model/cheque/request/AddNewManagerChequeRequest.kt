package com.zaed.common.data.model.cheque.request

import com.zaed.common.data.model.cheque.ManagerCheque

data class AddNewManagerChequeRequest(
    val managerCheque: ManagerCheque = ManagerCheque(),
)
package com.zaed.common.data.model.sale.request

import com.zaed.common.data.model.sale.StoreTransaction

data class UpdateStoreSaleRequest(
    val sale: StoreTransaction,
    val employeeId: String,
    val employeeName: String,
)

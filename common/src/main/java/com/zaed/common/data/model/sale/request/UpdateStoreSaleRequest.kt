package com.zaed.common.data.model.sale.request

import com.zaed.common.data.model.sale.StoreSale

data class UpdateStoreSaleRequest(
    val sale: StoreSale,
    val employeeId: String,
    val employeeName: String,
)

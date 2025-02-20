package com.zaed.common.data.model.sale.request

data class DeleteStoreSaleRequest(
    val saleId: String,
    val employeeId: String,
    val employeeName: String,
)
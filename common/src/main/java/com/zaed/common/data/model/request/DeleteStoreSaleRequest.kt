package com.zaed.common.data.model.request

data class DeleteStoreSaleRequest(
    val saleId: String,
    val employeeId: String,
    val employeeName: String,
)
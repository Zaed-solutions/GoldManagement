package com.zaed.common.data.model.request

data class DeleteLossRequest(
    val lossId: String,
    val employeeId: String,
    val employeeName: String
)

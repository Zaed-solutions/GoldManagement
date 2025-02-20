package com.zaed.common.data.model.loss.request

data class DeleteLossRequest(
    val lossId: String,
    val employeeId: String,
    val employeeName: String
)

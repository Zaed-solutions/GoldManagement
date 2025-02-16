package com.zaed.common.data.model

import java.util.Date

data class ChangeLog(
    val date: Date = Date(),
    val employeeId: String = "",
    val employeeName: String = "",
    val action: String = "",
)

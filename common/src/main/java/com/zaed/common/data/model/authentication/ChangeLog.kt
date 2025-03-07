package com.zaed.common.data.model.authentication

import java.util.Date

data class ChangeLog(
    val date: Date = Date(),
    val employeeId: String = "",
    val employeeName: String = "",
    val type: LogType = LogType.CREATE,
)

enum class LogType {
    CREATE,
    UPDATE,
    DELETE
}
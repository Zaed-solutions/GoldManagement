package com.zaed.common.data.model

import java.util.Date

data class Loss(
    val id: String = "",
    val value: Double = 0.0,
    val reason: String = "",
    val date: Date = Date(),
    val userId: String = "",
    val userName: String = "",
    val storeId: String = "",
    val storeName: String = "",
    val deleted: Boolean = false,
    val logs: List<ChangeLog> = emptyList()
)


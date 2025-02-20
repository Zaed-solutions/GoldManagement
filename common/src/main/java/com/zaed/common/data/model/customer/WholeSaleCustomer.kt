package com.zaed.common.data.model.customer

import com.zaed.common.data.model.authentication.ChangeLog
import java.util.Date

data class WholeSaleCustomer(
    val id: String = "",
    val distributorId: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val address: String = "",
    val city: String = "",
    val zone: Zone = Zone.NOT_DEFINED,
    val createdAt: Date = Date(),
    val inDebt: Boolean = false,
    val debtAmount: Double = 0.0,
    val deleted: Boolean = false,
    val logs :List <ChangeLog> = emptyList()
    )




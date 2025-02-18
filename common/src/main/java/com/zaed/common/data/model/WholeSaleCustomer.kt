package com.zaed.common.data.model

import java.util.Date

data class WholeSaleCustomer(
    val id: String = "",
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




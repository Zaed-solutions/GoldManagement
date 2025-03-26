package com.zaed.common.data.model.customer

import com.zaed.common.data.model.authentication.ChangeLog
import kotlinx.serialization.Transient
import java.util.Date
abstract class Account(
    open val id: String = "",
    open val name: String = "",
    open val phone: String = "",
    open val email: String = "",
    open val note: String = "",
    open val createdAt: Date = Date(),
    open val moneyDebtAmount: Double = 0.0,
    open val goldDebtAmount: Double = 0.0,
)
data class WholeSaleCustomer(
    override val id: String = "",
    val distributorId: String = "",
    override val name: String = "",
    override val phone: String = "",
    override val email: String = "",
    val address: String = "",
    val city: String = "",
    override val createdAt: Date = Date(),
    override val moneyDebtAmount: Double = 0.0,
    override val goldDebtAmount: Double = 0.0,
    override val note: String = "",
    val deleted: Boolean = false,
    val logs: List<ChangeLog> = emptyList()
): Account(
    id = id,
    name = name,
    phone = phone,
    email = email,
    note = note,
    createdAt = createdAt,
    moneyDebtAmount = moneyDebtAmount,
    goldDebtAmount = goldDebtAmount
) {
    @Transient
    val inDebt: Boolean
        get() = moneyDebtAmount > 0
}







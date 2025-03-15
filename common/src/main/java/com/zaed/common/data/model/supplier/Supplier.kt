package com.zaed.common.data.model.supplier

import com.zaed.common.data.model.customer.Account
import java.util.Date

data class Supplier(
    override val id: String = "",
    override val phone: String = "",
    override val name: String = "",
    override val email: String = "",
    override val createdAt: Date = Date(),
    override val debtAmount: Double = 0.0
):Account(
    id = id,
    name = name,
    phone = phone,
    email = email,
    createdAt = createdAt,
    debtAmount = debtAmount
)

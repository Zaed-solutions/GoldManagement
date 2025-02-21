package com.zaed.common.data.model.customer

import com.zaed.common.data.model.authentication.ChangeLog
import kotlinx.serialization.Transient
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
    val paymentArray : List<CustomerPayment> = emptyList(),
    val deleted: Boolean = false,
    val logs: List<ChangeLog> = emptyList()
) {
    @Transient
    val debtAmount : Double
        get() = paymentArray.sumOf { it.amount }

    @Transient
    val inDebt: Boolean
        get() = debtAmount > 0
}
data class CustomerPayment(
    val paymentId: String = "",
    val amount: Double = 0.0,
)




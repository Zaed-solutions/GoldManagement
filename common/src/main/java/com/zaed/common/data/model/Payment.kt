package com.zaed.common.data.model

data class Payment(
    val id :String ="",
    val type: PaymentType = PaymentType.CASH,
    val amount: Double = 0.0,
    val deleted: Boolean = false,
    val logs :List <ChangeLog> = emptyList()
)


package com.zaed.common.data.model.customer

data class AddWholeSaleCustomerRequest(
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val address: String = "",
    val city: String = "",
    val zone: Zone = Zone.NOT_DEFINED,
)
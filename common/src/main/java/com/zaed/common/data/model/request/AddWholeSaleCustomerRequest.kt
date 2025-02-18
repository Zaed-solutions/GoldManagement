package com.zaed.common.data.model.request

import com.zaed.common.data.model.Zone

data class AddWholeSaleCustomerRequest(
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val address: String = "",
    val city: String = "",
    val zone: Zone = Zone.NOT_DEFINED,
)
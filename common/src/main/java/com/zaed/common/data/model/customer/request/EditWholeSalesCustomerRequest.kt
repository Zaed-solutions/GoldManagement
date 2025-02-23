package com.zaed.common.data.model.customer.request

import com.zaed.common.data.model.customer.AddWholeSaleCustomerRequest

data class EditWholeSalesCustomerRequest (
    val id : String,
    val updatedData : AddWholeSaleCustomerRequest
)
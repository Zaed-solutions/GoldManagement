package com.zaed.common.data.model.customer

data class FetchWholesaleCustomersByNameRequest(
    val name: String = "",
    val distributorId: String = ""
)
data class FetchSuppliersByNameRequest(
    val name: String = "",
    val distributorId: String = ""
)

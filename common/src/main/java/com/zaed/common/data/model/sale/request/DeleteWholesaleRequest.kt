package com.zaed.common.data.model.sale.request

data class DeleteWholesaleRequest(
    val saleId: String = "",
    val distributorId: String = "",
    val distributorName: String = ""
)

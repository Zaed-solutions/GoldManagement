package com.zaed.common.data.model.purchase.request

data class FetchSupplierPurchasesRequest(
    val isManager: Boolean = false,
    val supplierId: String,
)

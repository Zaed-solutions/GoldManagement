package com.zaed.common.data.model.payment.request

data class FetchSupplierPaymentsRequest(
    val isManager: Boolean,
    val supplierId: String
)

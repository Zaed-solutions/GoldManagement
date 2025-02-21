package com.zaed.common.data.model.payment.request

data class FetchPaymentsByIdsRequest(
    val paymentsIds: List<String> = emptyList()
)

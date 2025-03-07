package com.zaed.common.data.model.sale

data class DatedIngotTransactions(
    val formattedDate: String = "",
    val totalEarnings: Double = 0.0,
    val transactions: List<IngotTransaction> = emptyList()
)

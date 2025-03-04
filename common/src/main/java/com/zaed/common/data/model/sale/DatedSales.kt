package com.zaed.common.data.model.sale

data class DatedSales(
    val formattedDate: String = "",
    val totalAmount: Double = 0.0,
    val sales: List<Sale> = emptyList()
)

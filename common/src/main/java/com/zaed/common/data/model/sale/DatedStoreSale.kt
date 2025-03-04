package com.zaed.common.data.model.sale

data class DatedStoreSale(
    val formattedDate: String = "",
    val totalSales: Double = 0.0,
    val sales: List<StoreSale> = emptyList()
)

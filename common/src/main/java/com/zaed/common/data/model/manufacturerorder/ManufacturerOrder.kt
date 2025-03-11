package com.zaed.common.data.model.manufacturerorder

import java.util.Date

data class ManufacturerOrder(
    val id: String = "",
    val orderNumber: String = "",
    val createdAt: Date = Date(),
    val rawAmount: Double = 0.0,
    val receivedAmount: Double = 0.0,
    val totalProcessingFee: Double = 0.0,
    val paidProcessingFee: Double = 0.0,
    val differenceOnManufacturer: Boolean = true,
    val closed: Boolean = false,
)

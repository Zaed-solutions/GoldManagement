package com.zaed.common.data.model.loss

import java.util.Date

data class DatedLoss(
    val formattedDate: String = "",
    val totalLosses: Double = 0.0,
    val losses: List<Loss> = emptyList()
)

package com.zaed.manager.ui.home.component

data class HomeSummary(
    val title: String,
    val iconRes: Int,
    val onClick: () -> Unit,
    val totalSales: Double,
    val totalLosses: Double = 0.0
)

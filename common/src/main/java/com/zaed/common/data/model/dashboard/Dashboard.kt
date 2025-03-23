package com.zaed.common.data.model.dashboard

data class Dashboard(
    val totalProfit: Double = 0.0,
    val totalLoss: Double = 0.0,
    val totalNetProfit: Double = 0.0,
    val totalStoreProfit: Double = 0.0,
    val totalStoreSales: Double = 0.0,
    val totalWholesaleSales: Double = 0.0,
    val totalManagerSales: Double = 0.0,
    val totalPurchases: Double = 0.0,
    val totalStoreLoss: Double = 0.0,
    val totalDistributorsLoss: Double = 0.0,
    val totalManagerLoss: Double = 0.0,
)
package com.zaed.common.data.repository

import com.zaed.common.data.model.dashboard.Dashboard

class DashboardRepositoryImpl : DashboardRepository {
    override suspend fun getDashboardData(): Result<Dashboard> {
        return Result.success(
            Dashboard(
                totalProfit = 15000.0,
                totalLoss = 6219.19,
                totalNetProfit = 8780.81,
                totalStoreSales = 24805.0,
                totalPurchases = 10580.0,
                totalManagerSales = 11250.0,
                totalStoreLoss = 11250.0,
                totalDistributorsLoss = 11250.0,
                totalManagerLoss = 11250.0,
                totalWholesaleSales = 11250.0,
            )
        )
    }
}
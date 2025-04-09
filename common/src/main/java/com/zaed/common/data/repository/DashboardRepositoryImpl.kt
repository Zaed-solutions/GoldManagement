package com.zaed.common.data.repository

import com.zaed.common.data.model.dashboard.DateFilter
import com.zaed.common.data.source.remote.DashboardRemoteSource

class DashboardRepositoryImpl(
    private val dashboardRemoteSource: DashboardRemoteSource
) : DashboardRepository {
    override suspend fun getStoresProfits(dateFilter: DateFilter): Result<Double> = dashboardRemoteSource.getStoresProfits(dateFilter)
    override suspend fun getWholesaleProfits(managerId: String,dateFilter: DateFilter): Result<Double>  = dashboardRemoteSource.getWholesaleProfits(managerId,dateFilter)

    override suspend fun getManagerProfits(managerId: String,dateFilter: DateFilter): Result<Double>  = dashboardRemoteSource.getManagerProfits(managerId,dateFilter)

    override suspend fun getStoreSales(dateFilter: DateFilter): Result<Double>  = dashboardRemoteSource.getStoreSales(dateFilter)

    override suspend fun getWholesaleSales(managerId: String,dateFilter: DateFilter): Result<Double>  = dashboardRemoteSource.getWholesaleSales(managerId,dateFilter)

    override suspend fun getManagerSales(managerId: String,dateFilter: DateFilter): Result<Double>  = dashboardRemoteSource.getManagerSales(managerId,dateFilter)

    override suspend fun getStoreLoss(dateFilter: DateFilter): Result<Double>  = dashboardRemoteSource.getStoreLoss(dateFilter)

    override suspend fun getWholesaleLoss(dateFilter: DateFilter): Result<Double>  = dashboardRemoteSource.getWholesaleLoss(dateFilter)

    override suspend fun getManagerLoss(dateFilter: DateFilter): Result<Double>  = dashboardRemoteSource.getManagerLoss(dateFilter)

    override suspend fun getGoldSales(dateFilter: DateFilter): Result<Double> {
        return dashboardRemoteSource.getGoldSales(dateFilter)
    }

    override suspend fun getSilverSales(dateFilter: DateFilter): Result<Double> {
        return dashboardRemoteSource.getSilverSales(dateFilter)
    }

    override suspend fun getIngotTransactions(dateFilter: DateFilter): Result<Double> {
        return dashboardRemoteSource.getIngotTransactions(dateFilter)
    }

}
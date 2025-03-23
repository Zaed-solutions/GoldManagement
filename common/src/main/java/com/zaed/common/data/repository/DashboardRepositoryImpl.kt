package com.zaed.common.data.repository

import com.zaed.common.data.source.remote.DashboardRemoteSource

class DashboardRepositoryImpl(
    private val dashboardRemoteSource: DashboardRemoteSource
) : DashboardRepository {
    override suspend fun getStoresProfits(): Result<Double> = dashboardRemoteSource.getStoresProfits()
    override suspend fun getWholesaleProfits(managerId: String): Result<Double>  = dashboardRemoteSource.getWholesaleProfits(managerId)

    override suspend fun getManagerProfits(managerId: String): Result<Double>  = dashboardRemoteSource.getManagerProfits(managerId)

    override suspend fun getStoreSales(): Result<Double>  = dashboardRemoteSource.getStoreSales()

    override suspend fun getWholesaleSales(managerId: String): Result<Double>  = dashboardRemoteSource.getWholesaleSales(managerId)

    override suspend fun getManagerSales(managerId: String): Result<Double>  = dashboardRemoteSource.getManagerSales(managerId)

    override suspend fun getStoreLoss(): Result<Double>  = dashboardRemoteSource.getStoreLoss()

    override suspend fun getWholesaleLoss(): Result<Double>  = dashboardRemoteSource.getWholesaleLoss()

    override suspend fun getManagerLoss(): Result<Double>  = dashboardRemoteSource.getManagerLoss()
}
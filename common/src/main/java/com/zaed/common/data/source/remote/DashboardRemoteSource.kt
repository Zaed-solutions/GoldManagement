package com.zaed.common.data.source.remote

import com.zaed.common.data.model.dashboard.DateFilter

interface DashboardRemoteSource {
    suspend fun getStoresProfits(dateFilter: DateFilter): Result<Double>
    suspend fun getWholesaleProfits(managerId: String,dateFilter: DateFilter): Result<Double>
    suspend fun getManagerProfits(managerId: String,dateFilter: DateFilter): Result<Double>
    suspend fun getStoreSales(dateFilter: DateFilter): Result<Double>
    suspend fun getManagerSales(managerId: String,dateFilter: DateFilter): Result<Double>
    suspend fun getWholesaleSales(managerId: String,dateFilter: DateFilter): Result<Double>
    suspend fun getStoreLoss(dateFilter: DateFilter): Result<Double>
    suspend fun getManagerLoss(dateFilter: DateFilter): Result<Double>
    suspend fun getWholesaleLoss(dateFilter: DateFilter): Result<Double>
    suspend fun getGoldSales(dateFilter: DateFilter): Result<Double>
    suspend fun getSilverSales(dateFilter: DateFilter): Result<Double>
    suspend fun getIngotTransactions(dateFilter: DateFilter): Result<Double>
}
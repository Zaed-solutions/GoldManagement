package com.zaed.common.data.repository

import com.zaed.common.data.model.dashboard.DateFilter

interface DashboardRepository {
    suspend fun getStoresProfits(dateFilter: DateFilter): Result<Double>
    suspend fun getWholesaleProfits(managerId: String,dateFilter: DateFilter): Result<Double>
    suspend fun getManagerProfits(managerId: String,dateFilter: DateFilter): Result<Double>
    suspend fun getStoreSales(dateFilter: DateFilter): Result<Double>
    suspend fun getWholesaleSales(managerId: String,dateFilter: DateFilter): Result<Double>
    suspend fun getManagerSales(managerId: String,dateFilter: DateFilter): Result<Double>
    suspend fun getStoreLoss(dateFilter: DateFilter): Result<Double>
    suspend fun getWholesaleLoss(dateFilter: DateFilter): Result<Double>
    suspend fun getManagerLoss(dateFilter: DateFilter): Result<Double>

}
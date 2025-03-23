package com.zaed.common.data.repository

interface DashboardRepository {
    suspend fun getStoresProfits(): Result<Double>
    suspend fun getWholesaleProfits(managerId: String): Result<Double>
    suspend fun getManagerProfits(managerId: String): Result<Double>
    suspend fun getStoreSales(): Result<Double>
    suspend fun getWholesaleSales(managerId: String): Result<Double>
    suspend fun getManagerSales(managerId: String): Result<Double>
    suspend fun getStoreLoss(): Result<Double>
    suspend fun getWholesaleLoss(): Result<Double>
    suspend fun getManagerLoss(): Result<Double>

}
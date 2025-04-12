package com.zaed.common.data.repository

import com.zaed.common.data.model.dashboard.DateFilter
import com.zaed.common.ui.addpurchase.ProductType

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
    suspend fun getGoldSales(dateFilter: DateFilter): Result<Double>
    suspend fun getSilverSales(dateFilter: DateFilter): Result<Double>
    suspend fun getIngotTransactions(dateFilter: DateFilter): Result<Double>
    suspend fun getProductDistributorSummary(dateFilter: DateFilter): Result<List<WholesaleDistributorSummary>>
    suspend fun getGoldDistributorSummary(dateFilter: DateFilter): Result<List<WholesaleDistributorSummary>>
    suspend fun getSilverDistributorSummary(dateFilter: DateFilter): Result<List<WholesaleDistributorSummary>>
    suspend fun getIngotDistributorSummary(dateFilter: DateFilter): Result<List<WholesaleDistributorSummary>>
}


data class WholesaleDistributorSummary(
    val distributorName :String,
    val profit: Double,
    val loss: Double,
    val sales: Double,
    val type :ProductType,
    val distributorId: String
)
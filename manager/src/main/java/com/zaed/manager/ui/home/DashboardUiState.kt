package com.zaed.manager.ui.home

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.dashboard.DateFilter

data class DashboardUiState(
    val currentUser: User = User(),
    val storesSales: Double = 0.0,
    val storesSalesLoading: Boolean = false,
    val storesProfit: Double = 0.0,
    val storesProfitLoading: Boolean = false,
    val storesLoss: Double = 0.0,
    val storesLossLoading: Boolean = false,
    val wholesaleSales: Double = 0.0,
    val wholesaleSalesLoading: Boolean = false,
    val wholesaleProfit: Double = 0.0,
    val wholesaleProfitLoading: Boolean = false,
    val wholesaleLoss: Double = 0.0,
    val wholesaleLossLoading: Boolean = false,
    val managerSales: Double = 0.0,
    val managerSalesLoading: Boolean = false,
    val managerProfit: Double = 0.0,
    val managerProfitLoading: Boolean = false,
    val managerLoss: Double = 0.0,
    val managerLossLoading: Boolean = false,
    val dateFilter: DateFilter = DateFilter(),
    val isLoading: Boolean = false,
    val error: String? = null
){
    val totalProfit
    get() = storesProfit + wholesaleProfit + managerProfit
    val totalLoss
    get() = storesLoss + wholesaleLoss + managerLoss
}
package com.zaed.manager.ui.home

import com.zaed.common.data.model.authentication.User
import com.zaed.manager.ui.home.component.DateFilter

data class DashboardUiState(
    val currentUser: User = User(),
    val storesSales: Double = 0.0,
    val storesProfit: Double = 0.0,
    val storesLoss: Double = 0.0,
    val wholesaleSales: Double = 0.0,
    val wholesaleProfit: Double = 0.0,
    val wholesaleLoss: Double = 0.0,
    val managerSales: Double = 0.0,
    val managerProfit: Double = 0.0,
    val managerLoss: Double = 0.0,
    val dateFilter: DateFilter = DateFilter(),
    val isLoading: Boolean = false,
    val error: String? = null
){
    val totalProfit
    get() = storesProfit + wholesaleProfit + managerProfit
    val totalLoss
    get() = storesLoss + wholesaleLoss + managerLoss
}
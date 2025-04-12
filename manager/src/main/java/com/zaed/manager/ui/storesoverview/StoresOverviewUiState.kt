package com.zaed.manager.ui.storesoverview

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.category.Category
import com.zaed.common.data.model.sale.StoreTransaction
import com.zaed.common.data.model.store.Store
import com.zaed.manager.ui.storessales.components.StoreSalesFilter

data class StoresOverviewUiState(
    val isStoresLoading: Boolean = true,
    val stores: List<Store> = emptyList(),
    val isStoresSalesLoading: Boolean = true,
    val employees: List<User> = emptyList(),
    val customers: Set<String> = emptySet(),
    val locations: Set<String> = emptySet(),
    val categories: List<Category> = emptyList(),
    val allSales: List<StoreTransaction> = emptyList(),
    val filteredSales: List<StoreTransaction> = emptyList(),
    val salesSearchQuery: String = "",
    val salesFilter: StoreSalesFilter = StoreSalesFilter()
){
    val totalSalesAmount: Double
        get() = filteredSales.sumOf { it.totalAmount }
}

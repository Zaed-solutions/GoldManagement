package com.zaed.manager.ui.storessales

import com.zaed.common.data.model.category.Category
import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.sale.StoreTransaction
import com.zaed.common.data.model.store.Store
import com.zaed.manager.ui.storessales.components.StoreSalesFilter

data class StoresSalesUiState(
    val isLoading: Boolean = true,
    val employees: List<User> = emptyList(),
    val customers: Set<String> = emptySet(),
    val stores: List<Store> = emptyList(),
    val locations: Set<String> = emptySet(),
    val categories: List<Category> = emptyList(),
    val allSales: List<StoreTransaction> = emptyList(),
    val filteredSales: List<StoreTransaction> = emptyList(),
    val searchQuery: String = "",
    val filter: StoreSalesFilter = StoreSalesFilter()
){
    val totalAmount: Double
        get() = filteredSales.sumOf { it.totalAmount }
}

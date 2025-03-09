package com.zaed.manager.ui.storessales

import androidx.compose.ui.util.fastSumBy
import com.zaed.common.data.model.Category
import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.sale.StoreSale
import com.zaed.common.data.model.store.Store
import com.zaed.manager.ui.storessales.components.StoreSalesFilter

data class StoresSalesUiState(
    val isLoading: Boolean = true,
    val employees: List<User> = emptyList(),
    val customers: Set<String> = emptySet(),
    val stores: List<Store> = emptyList(),
    val locations: Set<String> = emptySet(),
    val categories: List<Category> = emptyList(),
    val allSales: List<StoreSale> = emptyList(),
    val filteredSales: List<StoreSale> = emptyList(),
    val searchQuery: String = "",
    val filter: StoreSalesFilter = StoreSalesFilter()
){
    val totalAmount: Double
        get() = filteredSales.sumOf { it.totalAmount }
}

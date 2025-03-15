package com.zaed.manager.ui.distributorssales

import com.zaed.common.data.model.Category
import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.data.model.store.Store
import com.zaed.manager.ui.distributorssales.components.DistributorsSalesFilter

data class DistributorsSalesUiState(
    val isLoading: Boolean = true,
    val employees: List<User> = emptyList(),
    val customers: List<WholeSaleCustomer> = emptyList(),
    val stores: List<Store> = emptyList(),
    val locations: Set<String> = emptySet(),
    val categories: List<Category> = emptyList(),
    val allSales: List<WholesaleTransaction> = emptyList(),
    val filteredSales: List<WholesaleTransaction> = emptyList(),
    val searchQuery: String = "",
    val filter: DistributorsSalesFilter = DistributorsSalesFilter()
) {
    val totalAmount: Double
        get() = filteredSales.sumOf { it.totalAmount }
}

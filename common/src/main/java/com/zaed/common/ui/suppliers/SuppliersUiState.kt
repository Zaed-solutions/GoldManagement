package com.zaed.common.ui.suppliers

import com.zaed.common.data.model.supplier.Supplier

data class SuppliersUiState(
    val isLoading: Boolean = true,
    val isAdmin: Boolean = false,
    val allSuppliers: List<Supplier> = emptyList(),
    val searchQuery: String = "",
    val filteredSuppliers: List<Supplier> = emptyList(),
)

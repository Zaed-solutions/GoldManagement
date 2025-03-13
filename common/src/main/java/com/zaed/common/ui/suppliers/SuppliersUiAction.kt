package com.zaed.common.ui.suppliers

import com.zaed.common.data.model.supplier.Supplier

sealed interface SuppliersUiAction {
    data object OnShowNavDrawer : SuppliersUiAction
    data class UpdateSearchQuery(val query: String) : SuppliersUiAction
    data class OnSupplierClicked(val supplierId: String) : SuppliersUiAction
    data class AddSupplier(val supplier: Supplier): SuppliersUiAction
    data class UpdateSupplier(val supplier: Supplier): SuppliersUiAction
    data class DeleteSupplier(val supplier: Supplier): SuppliersUiAction
}
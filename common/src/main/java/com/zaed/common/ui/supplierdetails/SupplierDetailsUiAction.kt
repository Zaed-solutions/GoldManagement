package com.zaed.common.ui.supplierdetails

import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.supplier.Supplier

sealed interface SupplierDetailsUiAction {
    data object OnBackClicked : SupplierDetailsUiAction
    data class DeletePayment(val payment: Payment): SupplierDetailsUiAction
    data class DeletePurchase(val purchaseId: String): SupplierDetailsUiAction
    data class UpdatePurchasesSearchQuery(val query: String): SupplierDetailsUiAction
    data class UpdatePayment(val oldPayment: Payment, val newPayment: Payment): SupplierDetailsUiAction
    data class AddPayment(val payment: Payment): SupplierDetailsUiAction
    data class UpdateSupplier(val supplier: Supplier): SupplierDetailsUiAction
    data class OnEditPurchase(val purchaseId: String): SupplierDetailsUiAction
    data class OnPurchaseClicked(val purchaseId: String): SupplierDetailsUiAction
}
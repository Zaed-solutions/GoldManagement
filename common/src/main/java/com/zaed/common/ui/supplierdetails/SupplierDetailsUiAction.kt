package com.zaed.common.ui.supplierdetails

import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.supplier.Supplier

sealed interface SupplierDetailsUiAction {
    data object OnBackClicked : SupplierDetailsUiAction
    data class DeletePayment(val payment: Payment): SupplierDetailsUiAction
    data class UpdatePayment(val oldPayment: Payment, val newPayment: Payment): SupplierDetailsUiAction
    data class AddPayment(val payment: Payment): SupplierDetailsUiAction
    data class UpdateSupplier(val supplier: Supplier): SupplierDetailsUiAction
}
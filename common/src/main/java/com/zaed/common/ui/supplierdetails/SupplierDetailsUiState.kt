package com.zaed.common.ui.supplierdetails

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.supplier.Supplier

data class SupplierDetailsUiState(
    val supplier: Supplier = Supplier(),
    val credit: Double = 0.0,
    val payments: List<Payment> = emptyList(),
    val currentUser: User = User()
)

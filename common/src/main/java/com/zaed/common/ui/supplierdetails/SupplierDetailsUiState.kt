package com.zaed.common.ui.supplierdetails

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.purchase.Purchase
import com.zaed.common.data.model.supplier.Supplier

data class SupplierDetailsUiState(
    val supplier: Supplier = Supplier(),
    val credit: Double = 0.0,
    val purchasesSearchQuery: String = "",
    val allPurchases: List<Purchase> = emptyList(),
    val filteredPurchases: List<Purchase> = emptyList(),
    val payments: List<Payment> = emptyList(),
    val currentUser: User = User(),
)

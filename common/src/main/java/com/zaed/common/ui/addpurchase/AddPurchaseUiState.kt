package com.zaed.common.ui.addpurchase

import com.zaed.common.data.model.Category
import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.purchase.Purchase
import com.zaed.common.data.model.supplier.Supplier

data class AddPurchaseUiState(
    val isFinished: Boolean = false,
    val isLoading: Boolean = false,
    val initialPurchase: Purchase = Purchase(),
    val purchase: Purchase = Purchase(),
    val selectedSupplier: Supplier = Supplier(),
    val supplierSearchQuery: String = "",
    val suggestedSuppliers: List<Supplier> = emptyList(),
    val allSuppliers: List<Supplier> = emptyList(),
    val currentUser: User = User(),
    val totalPaid: Double = 0.0,
    val isAdmin: Boolean = true/*todo*/,
    val payments: List<Payment> = emptyList(),
    val categories: List<Category> = emptyList(),
)

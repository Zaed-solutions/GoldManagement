package com.zaed.common.ui.addpurchase

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.category.Category
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.data.model.supplier.Supplier

data class AddPurchaseUiState(
    val isFinished: Boolean = false,
    val isLoading: Boolean = false,
    val initialPurchase: WholesaleTransaction =WholesaleTransaction(),
    val purchase: WholesaleTransaction =WholesaleTransaction(),
    val selectedSupplier: Supplier = Supplier(),
    val supplierSearchQuery: String = "",
    val suggestedSuppliers: List<Supplier> = emptyList(),
    val allSuppliers: List<Supplier> = emptyList(),
    val currentUser: User = User(),
    val selectedProductType: ProductType = ProductType.PRODUCT,
    val isAdmin: Boolean = true/*todo*/,
    val payments: List<Payment> = emptyList(),
    val categories: List<Category> = emptyList(),
    val salesCheques: List<ChequePayment> = emptyList()
){
    val totalPaid
        get() = payments.filter { it.type != PaymentType.FUTURES }
            .sumOf { if (it.type == PaymentType.REMAIN) it.amount.unaryMinus() else it.amount }
    val totalFuturePaid get() = payments.filter { it.type == PaymentType.FUTURES }.sumOf { it.amount }
}

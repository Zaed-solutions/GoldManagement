package com.zaed.manager.ui.purchases

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.payment.PaymentStatus
import com.zaed.common.data.model.sale.DatedSales
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.ui.util.DateFormat

data class PurchasesUiState(
    val isLoading: Boolean = true,
    val isSignedOut: Boolean = false,
    val searchQuery: String = "",
    val currentUser: User = User(),
    val selectedPaymentStatus: PaymentStatus = PaymentStatus.ALL,
    val allPurchases: List<WholesaleTransaction> = emptyList(),
    val filteredPurchases: List<WholesaleTransaction> = emptyList(),
    val dateFilter: DateFormat = DateFormat.DATE,
    val datedSales: List<DatedSales> = emptyList()
)

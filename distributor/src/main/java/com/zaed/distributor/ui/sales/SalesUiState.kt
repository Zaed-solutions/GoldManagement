package com.zaed.distributor.ui.sales

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.payment.PaymentStatus
import com.zaed.common.data.model.sale.DatedSales
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.ui.util.DateFormat
import java.util.Date

data class SalesUiState(
    val isLoading: Boolean = true,
    val isSignedOut: Boolean = false,
    val searchQuery: String = "",
    val currentUser: User = User(),
    val selectedPaymentStatus: PaymentStatus = PaymentStatus.ALL,
    val allSales: List<WholesaleTransaction> = emptyList(),
    val filteredSales: List<WholesaleTransaction> = emptyList(),
    val dateFilter: DateFormat = DateFormat.DATE,
    val selectedRange: Pair<Date?, Date?> = null to null,
    val datedSales: List<DatedSales> = emptyList()
)

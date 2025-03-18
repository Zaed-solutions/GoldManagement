package com.zaed.common.ui.purchaseDetails

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.customer.Account
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.data.model.supplier.Supplier

data class PurchaseDetailsUiState(
    val isLoading: Boolean = false,
    val isPurchaseDeleted: Boolean = false,
    val purchase: WholesaleTransaction = WholesaleTransaction(),
    val currentUser: User = User(),
    val supplier: Account = Supplier(),
    val cashPayments: List<Payment> = emptyList()
)

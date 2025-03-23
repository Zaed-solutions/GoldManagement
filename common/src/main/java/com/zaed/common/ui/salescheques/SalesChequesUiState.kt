package com.zaed.common.ui.salescheques

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.cheque.ChequeStatus
import com.zaed.common.data.model.cheque.ManagerCheque
import com.zaed.common.data.model.customer.Account
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.data.model.supplier.Supplier

data class SalesChequesUiState(
    val loading: Boolean = false,
    val selectedAccount: Account = WholeSaleCustomer(),
    val currentUser: User = User(),
    val allSalesCheques:  List<ChequePayment> = emptyList(),
    val uncashedSalesCheques: List<ChequePayment> = emptyList(),
    val filteredSalesCheques:  List<ChequePayment> = emptyList(),
    val allManagerCheques: List<ManagerCheque> = emptyList(),
    val filteredManagerCheques: List<ManagerCheque> = emptyList(),
    val sales : List<WholesaleTransaction> = emptyList(),
    val customerSearchQuery: String = "",
    val suggestedCustomers: List<WholeSaleCustomer> = emptyList(),
    //
    val isAdmin: Boolean = false,
    val allSuppliers: List<Supplier> = emptyList(),
    val searchQuery: String = "",
    val filteredSuppliers: List<Supplier> = emptyList(),
    val chequeSearchQuery: String = "",
    val selectedChequeFilter: ChequeStatus? = null
)
package com.zaed.manager.ui.salescheques

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.cheque.ManagerCheque
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.data.model.supplier.Supplier

data class SalesChequesUiState(
    val loading: Boolean = false,
    val selectedCustomer: WholeSaleCustomer = WholeSaleCustomer(),
    val currentDistributor: User = User(),
    val salesPayments:  List<ChequePayment> = emptyList(),
    val managerPayments: List<ManagerCheque> = emptyList(),
    val sales : List<WholesaleTransaction> = emptyList(),
    val customerSearchQuery: String = "",
    val suggestedCustomers: List<WholeSaleCustomer> = emptyList(),
    //
    val isAdmin: Boolean = false,
    val allSuppliers: List<Supplier> = emptyList(),
    val searchQuery: String = "",
    val filteredSuppliers: List<Supplier> = emptyList(),
    val selectedSupplier: Supplier = Supplier()
)
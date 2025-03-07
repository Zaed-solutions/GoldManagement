package com.zaed.manager.ui.distributordetails

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.inventory.Inventory
import com.zaed.common.data.model.loss.DatedLoss
import com.zaed.common.data.model.loss.DistributorLoss
import com.zaed.common.data.model.sale.DatedIngotTransactions
import com.zaed.common.data.model.sale.DatedSales
import com.zaed.common.data.model.sale.IngotTransaction
import com.zaed.common.data.model.sale.WholesaleSale
import com.zaed.common.ui.util.DateFormat

data class DistributorDetailsUiState(
    val isLoading: Boolean = false,
    val distributor: User = User(),
    val salesQuery: String = "",
    val selectedSalesFilter: DateFormat = DateFormat.DATE,
    val allSales: List<WholesaleSale> = emptyList(),
    val filteredSales: List<WholesaleSale> = emptyList(),
    val datedSales: List<DatedSales> = emptyList(),
    val inventoryQuery: String = "",
    val mainInventories: List<Inventory> = emptyList(),
    val allInventories: List<Inventory> = emptyList(),
    val displayedInventories: List<Inventory> = emptyList(),
    val selectedLossesFilter: DateFormat = DateFormat.DATE,
    val allLosses: List<DistributorLoss> = emptyList(),
    val datedLosses: List<DatedLoss> = emptyList(),
    val allIngotTransactions: List<IngotTransaction> = emptyList(),
    val datedIngotTransactions: List<DatedIngotTransactions> = emptyList(),
    val ingotTransactionsDateFormat: DateFormat = DateFormat.DATE,
)

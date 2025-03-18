package com.zaed.manager.ui.distributordetails

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.inventory.Inventory
import com.zaed.common.data.model.loss.DatedLoss
import com.zaed.common.data.model.loss.DistributorLoss
import com.zaed.common.data.model.sale.DatedIngotTransactions
import com.zaed.common.data.model.sale.DatedSales
import com.zaed.common.data.model.sale.IngotTransaction
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.ui.util.DateFormat
import java.util.Date

data class DistributorDetailsUiState(
    val isLoading: Boolean = false,
    val distributor: User = User(),
    val salesQuery: String = "",
    val selectedSalesFilter: DateFormat = DateFormat.DATE,
    val selectedDateRange: Pair<Date?, Date?> = null to null,
    val allSales: List<WholesaleTransaction> = emptyList(),
    val filteredSales: List<WholesaleTransaction> = emptyList(),
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

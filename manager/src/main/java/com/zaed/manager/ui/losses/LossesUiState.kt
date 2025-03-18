package com.zaed.manager.ui.losses

import com.zaed.common.data.model.loss.DatedLoss
import com.zaed.common.data.model.loss.ManagerLoss
import com.zaed.common.ui.util.DateFormat
import java.util.Date

data class LossesUiState(
    val isLoading: Boolean = true,
    val personalExpenses: List<ManagerLoss> = emptyList(),
    val filteredPersonalExpenses : List<ManagerLoss> = emptyList(),
    val datedPersonalExpenses: List<DatedLoss> = emptyList(),
    val selectedPersonalExpensesFilter: DateFormat = DateFormat.DATE,
    val personalExpensesDateRange: Pair<Date?, Date?> = null to null,
    val losses: List<ManagerLoss> = emptyList(),
    val filteredLosses: List<ManagerLoss> = emptyList(),
    val datedLosses: List<DatedLoss> = emptyList(),
    val selectedLossesFilter: DateFormat = DateFormat.DATE,
    val lossesDateRange: Pair<Date?, Date?> = null to null
)

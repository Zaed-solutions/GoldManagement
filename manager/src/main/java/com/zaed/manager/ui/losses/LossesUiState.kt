package com.zaed.manager.ui.losses

import com.zaed.common.data.model.loss.DatedLoss
import com.zaed.common.data.model.loss.ManagerLoss
import com.zaed.common.ui.util.DateFormat

data class LossesUiState(
    val isLoading: Boolean = true,
    val personalExpenses: List<ManagerLoss> = emptyList(),
    val datedPersonalExpenses: List<DatedLoss> = emptyList(),
    val selectedPersonalExpensesFilter: DateFormat = DateFormat.DATE,
    val losses: List<ManagerLoss> = emptyList(),
    val datedLosses: List<DatedLoss> = emptyList(),
    val selectedLossesFilter: DateFormat = DateFormat.DATE
)

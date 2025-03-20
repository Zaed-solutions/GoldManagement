package com.zaed.distributor.ui.losses

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.loss.DatedLoss
import com.zaed.common.data.model.loss.DistributorLoss
import com.zaed.common.ui.util.DateFormat
import java.util.Date

data class LossesUiState(
    val isLoading: Boolean = false,
    val losses: List<DistributorLoss> = emptyList(),
    val filteredLosses: List<DistributorLoss> = emptyList(),
    val selectedDateFilter: DateFormat = DateFormat.DATE,
    val selectedDateRange: Pair<Date?, Date?> = Pair(null, null),
    val datedLosses: List<DatedLoss> = emptyList(),
    val currentUser: User = User()
)

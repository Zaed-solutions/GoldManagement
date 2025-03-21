package com.zaed.manager.ui.home

import com.zaed.common.data.model.dashboard.Dashboard
import com.zaed.manager.ui.home.component.DateFilter

data class DashboardUiState(
    val dashboardData: Dashboard = Dashboard(),
    val dateFilter: DateFilter = DateFilter(),
    val isLoading: Boolean = false,
    val error: String? = null
)
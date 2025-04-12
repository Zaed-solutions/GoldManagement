package com.zaed.manager.ui.home

import com.zaed.common.data.model.dashboard.DateFilter
import com.zaed.common.data.model.dashboard.DateFilterType
import com.zaed.manager.ui.home.component.ReportType
import kotlinx.datetime.Month
import java.util.Date

sealed interface DashboardUiAction {
    data class NavigateToDetail(val reportType: ReportType) : DashboardUiAction
    data class UpdateDateFilter(val dateFilter: DateFilter) : DashboardUiAction
    data object ToggleDateFilterVisibility : DashboardUiAction
    data class SelectDateFilterType(val filterType: DateFilterType) : DashboardUiAction
    data class SelectYear(val year: Int) : DashboardUiAction
    data class SelectMonth(val month: Month) : DashboardUiAction
    data class SelectDay(val day: Int) : DashboardUiAction
    data class SelectDateRange(val startDate: Date, val endDate: Date) : DashboardUiAction
    data object ApplyDateFilter : DashboardUiAction
    data object OnShowNavDrawer : DashboardUiAction
    data object OnStoresClicked : DashboardUiAction
    data object OnGoldSalesClicked : DashboardUiAction
    data object OnSilverSalesClicked : DashboardUiAction
    data object OnIngotTransactionsClicked : DashboardUiAction
    data class OnDistributorsSalesClicked(val distributorId: String) : DashboardUiAction
    data object ReloadAllData : DashboardUiAction
    data object OnBackClicked : DashboardUiAction
}
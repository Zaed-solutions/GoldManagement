package com.zaed.manager.ui.home

import com.zaed.manager.ui.home.component.DateFilter
import com.zaed.manager.ui.home.component.DateFilterType
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
}
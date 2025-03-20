package com.zaed.manager.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.repository.DashboardRepository
import com.zaed.manager.ui.home.component.DateFilter
import com.zaed.manager.ui.home.component.DateFilterType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Month
import java.util.Calendar
import java.util.Date

class DashboardViewModel(
    private val repository: DashboardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun handleAction(action: DashboardUiAction) {
        when (action) {
            is DashboardUiAction.UpdateDateFilter -> updateDateFilter(action.dateFilter)
            is DashboardUiAction.ToggleDateFilterVisibility -> toggleDateFilterVisibility()
            is DashboardUiAction.SelectDateFilterType -> selectDateFilterType(action.filterType)
            is DashboardUiAction.SelectYear -> selectYear(action.year)
            is DashboardUiAction.SelectMonth -> selectMonth(action.month)
            is DashboardUiAction.SelectDay -> selectDay(action.day)
            is DashboardUiAction.SelectDateRange -> selectDateRange(
                action.startDate,
                action.endDate
            )

            is DashboardUiAction.ApplyDateFilter -> applyDateFilter()
            else -> {}
        }
    }

    private fun loadDashboardData() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }

            repository.getDashboardData().onSuccess { data ->
                _uiState.update {
                    it.copy(
                        dashboardData = data, isLoading = false, error = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false, error = error.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }

    private fun updateDateFilter(dateFilter: DateFilter) {
        _uiState.update { it.copy(dateFilter = dateFilter) }
    }

    private fun toggleDateFilterVisibility() {
        _uiState.update {
            it.copy(
                dateFilter = it.dateFilter.copy(
                    isFilterVisible = !it.dateFilter.isFilterVisible
                )
            )
        }
    }

    private fun selectDateFilterType(filterType: DateFilterType) {
        _uiState.update {
            it.copy(
                dateFilter = it.dateFilter.copy(
                    filterType = filterType
                )
            )
        }
    }

    private fun selectYear(year: Int) {
        _uiState.update {
            it.copy(
                dateFilter = it.dateFilter.copy(
                    selectedYear = year
                )
            )
        }
    }

    private fun selectMonth(month: Month) {
        _uiState.update {
            it.copy(
                dateFilter = it.dateFilter.copy(
                    selectedMonth = month
                )
            )
        }
    }

    private fun selectDay(day: Int) {
        _uiState.update {
            it.copy(
                dateFilter = it.dateFilter.copy(
                    selectedDay = day
                )
            )
        }
    }

    private fun selectDateRange(startDate: Date, endDate: Date) {
        _uiState.update {
            it.copy(
                dateFilter = it.dateFilter.copy(
                    startDate = startDate,
                    endDate = endDate
                )
            )
        }
    }

    private fun applyDateFilter() {
        val currentFilter = _uiState.value.dateFilter
        val (startDate, endDate) = when (currentFilter.filterType) {
            DateFilterType.DAY -> {
                val calendar = Calendar.getInstance()
                calendar.set(
                    currentFilter.selectedYear,
                    currentFilter.selectedMonth.value - 1,
                    currentFilter.selectedDay
                )
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val date = calendar.time
                Pair(date, date)
            }

            DateFilterType.MONTH -> {
                val firstDayCalendar = Calendar.getInstance()
                firstDayCalendar.set(
                    currentFilter.selectedYear,
                    currentFilter.selectedMonth.value - 1,
                    1
                )
                firstDayCalendar.set(Calendar.HOUR_OF_DAY, 0)
                firstDayCalendar.set(Calendar.MINUTE, 0)
                firstDayCalendar.set(Calendar.SECOND, 0)
                firstDayCalendar.set(Calendar.MILLISECOND, 0)

                val lastDayCalendar = Calendar.getInstance()
                lastDayCalendar.set(
                    currentFilter.selectedYear,
                    currentFilter.selectedMonth.value - 1,
                    1
                )
                lastDayCalendar.set(
                    Calendar.DAY_OF_MONTH,
                    lastDayCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                )
                lastDayCalendar.set(Calendar.HOUR_OF_DAY, 23)
                lastDayCalendar.set(Calendar.MINUTE, 59)
                lastDayCalendar.set(Calendar.SECOND, 59)
                lastDayCalendar.set(Calendar.MILLISECOND, 999)

                Pair(firstDayCalendar.time, lastDayCalendar.time)
            }

            DateFilterType.YEAR -> {
                val firstDayCalendar = Calendar.getInstance()
                firstDayCalendar.set(currentFilter.selectedYear, Calendar.JANUARY, 1)
                firstDayCalendar.set(Calendar.HOUR_OF_DAY, 0)
                firstDayCalendar.set(Calendar.MINUTE, 0)
                firstDayCalendar.set(Calendar.SECOND, 0)
                firstDayCalendar.set(Calendar.MILLISECOND, 0)

                val lastDayCalendar = Calendar.getInstance()
                lastDayCalendar.set(currentFilter.selectedYear, Calendar.DECEMBER, 31)
                lastDayCalendar.set(Calendar.HOUR_OF_DAY, 23)
                lastDayCalendar.set(Calendar.MINUTE, 59)
                lastDayCalendar.set(Calendar.SECOND, 59)
                lastDayCalendar.set(Calendar.MILLISECOND, 999)

                Pair(firstDayCalendar.time, lastDayCalendar.time)
            }

            DateFilterType.RANGE -> {
                Pair(
                    currentFilter.startDate ?: Calendar.getInstance().time,
                    currentFilter.endDate ?: Calendar.getInstance().time
                )
            }
        }

        _uiState.update {
            it.copy(
                dateFilter = it.dateFilter.copy(
                    startDate = startDate,
                    endDate = endDate,
                    isFilterVisible = false
                )
            )
        }

        loadFilteredDashboardData(startDate, endDate)
    }

    private fun loadFilteredDashboardData(startDate: Date, endDate: Date) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }

            //TODO USE DATES
            delay(500)

            repository.getDashboardData().onSuccess { data ->
                _uiState.update {
                    it.copy(
                        dashboardData = data, isLoading = false, error = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false, error = error.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }
}
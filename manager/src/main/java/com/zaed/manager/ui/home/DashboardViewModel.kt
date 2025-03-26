package com.zaed.manager.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.dashboard.DateFilter
import com.zaed.common.data.model.dashboard.DateFilterType
import com.zaed.common.data.repository.DashboardRepository
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Month
import java.util.Calendar
import java.util.Date

class DashboardViewModel(
    private val repository: DashboardRepository,
    private val getCurrentUserLoggedInUseCase: GetCurrentUserLoggedInUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUserLoggedInUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.update {
                        it.copy(
                            currentUser = data, isLoading = false, error = null
                        )
                    }
                    loadAllData()
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

    private fun loadAllData() {
        loadStoresProfits()
        loadWholeSalesProfits()
        loadManagerProfits()
        loadStoreSales()
        loadManagerSales()
        loadWholesaleSales()
        loadStoreLoss()
        loadWholesaleLoss()
        loadManagerLoss()
    }

    private fun loadManagerLoss() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(managerLossLoading = true) }

            repository.getManagerLoss(dateFilter = uiState.value.dateFilter).onSuccess { data ->
                _uiState.update {
                    it.copy(
                        managerLoss = data, managerLossLoading = false, error = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        managerLossLoading = false,
                        error = error.message ?: "Unknown error occurred"
                    )
                }
            }
        }

    }

    private fun loadWholesaleLoss() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(wholesaleLossLoading = true) }

            repository.getWholesaleLoss(dateFilter = uiState.value.dateFilter).onSuccess { data ->
                _uiState.update {
                    it.copy(
                        wholesaleLoss = data, wholesaleLossLoading = false, error = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        wholesaleLossLoading = false,
                        error = error.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }

    private fun loadStoreLoss() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(storesLossLoading = true) }

            repository.getStoreLoss(dateFilter = uiState.value.dateFilter).onSuccess { data ->
                _uiState.update {
                    it.copy(
                        storesLoss = data, storesLossLoading = false, error = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        storesLossLoading = false, error = error.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }

    private fun loadWholesaleSales() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(wholesaleSalesLoading = true) }

            repository.getWholesaleSales(uiState.value.currentUser.id,dateFilter = uiState.value.dateFilter).onSuccess { data ->
                _uiState.update {
                    it.copy(
                        wholesaleSales = data, wholesaleSalesLoading = false, error = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        wholesaleSalesLoading = false,
                        error = error.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }

    private fun loadManagerSales() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(managerSalesLoading = true) }

            repository.getManagerSales(uiState.value.currentUser.id,dateFilter = uiState.value.dateFilter).onSuccess { data ->
                _uiState.update {
                    it.copy(
                        managerSales = data, managerSalesLoading = false, error = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        managerSalesLoading = false, error = error.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }

    private fun loadManagerProfits() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(managerProfitLoading = true) }

            repository.getManagerProfits(uiState.value.currentUser.id,dateFilter = uiState.value.dateFilter).onSuccess { data ->
                _uiState.update {
                    it.copy(
                        managerProfit = data, managerProfitLoading = false, error = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        managerProfitLoading = false, error = error.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }

    private fun loadStoreSales() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(storesSalesLoading = true) }

            repository.getStoreSales(dateFilter = uiState.value.dateFilter).onSuccess { data ->
                _uiState.update {
                    it.copy(
                        storesSales = data, storesSalesLoading = false, error = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        storesSalesLoading = false, error = error.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }

    fun handleAction(action: DashboardUiAction) {
        when (action) {
            DashboardUiAction.ReloadAllData -> loadAllData()
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

    private fun loadStoresProfits() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(storesProfitLoading = true) }

            repository.getStoresProfits(dateFilter = uiState.value.dateFilter).onSuccess { data ->
                _uiState.update {
                    it.copy(
                        storesProfit = data, storesProfitLoading = false, error = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        storesProfitLoading = false, error = error.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }

    private fun loadWholeSalesProfits() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(wholesaleProfitLoading = true) }

            repository.getWholesaleProfits(uiState.value.currentUser.id,dateFilter = uiState.value.dateFilter).onSuccess { data ->
                _uiState.update {
                    it.copy(
                        wholesaleProfit = data, wholesaleProfitLoading = false, error = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        wholesaleProfitLoading = false, error = error.message ?: "Unknown error occurred"
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
                val startOfDay = calendar.time

                calendar.set(Calendar.HOUR_OF_DAY, 23)
                calendar.set(Calendar.MINUTE, 59)
                calendar.set(Calendar.SECOND, 59)
                calendar.set(Calendar.MILLISECOND, 999)
                val endOfDay = calendar.time

                Pair(startOfDay, endOfDay)
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
                val calendar = Calendar.getInstance()

                calendar.time = currentFilter.startDate ?: Calendar.getInstance().time
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val startOfFirstDate = calendar.time

                calendar.time = currentFilter.endDate ?: Calendar.getInstance().time
                calendar.set(Calendar.HOUR_OF_DAY, 23)
                calendar.set(Calendar.MINUTE, 59)
                calendar.set(Calendar.SECOND, 59)
                calendar.set(Calendar.MILLISECOND, 999)
                val endOfSecondDate = calendar.time

                Pair(startOfFirstDate, endOfSecondDate)
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

        loadAllData()
    }


}
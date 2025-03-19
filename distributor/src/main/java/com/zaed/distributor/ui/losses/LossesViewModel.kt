package com.zaed.distributor.ui.losses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.authentication.ChangeLog
import com.zaed.common.data.model.authentication.LogType
import com.zaed.common.data.model.loss.DistributorLoss
import com.zaed.common.data.model.loss.request.AddDistributorLossRequest
import com.zaed.common.data.model.loss.request.FetchDistributorLossesRequest
import com.zaed.common.data.model.loss.request.UpdateDistributorLossRequest
import com.zaed.common.domain.authentication.GetCurrentUserLoggedInUseCase
import com.zaed.common.domain.loss.AddDistributorLossUseCase
import com.zaed.common.domain.loss.ConvertLossesToDatedLossesUseCase
import com.zaed.common.domain.loss.FetchDistributorLossesUseCase
import com.zaed.common.domain.loss.UpdateDistributorLossUseCase
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.isAfter
import com.zaed.common.ui.util.isBefore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class LossesViewModel(
    private val getCurrentUserUseCase: GetCurrentUserLoggedInUseCase,
    private val convertToDatedLossesUseCase: ConvertLossesToDatedLossesUseCase,
    private val fetchLossesUseCase: FetchDistributorLossesUseCase,
    private val addLossUseCase: AddDistributorLossUseCase,
    private val updateLossUseCase: UpdateDistributorLossUseCase,
    private val deleteLossUseCase: UpdateDistributorLossUseCase
) : ViewModel() {
    private val TAG = "LossesViewModel"
    private val _uiState = MutableStateFlow(LossesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUserUseCase().collect { result ->
                result.onSuccess { data ->
                    _uiState.update { oldState ->
                        oldState.copy(currentUser = data)
                    }
                    fetchDistributorLosses(data.id)
                }.onFailure { e ->
                    Log.e(TAG, "fetchCurrentUser: ${e.message}", e)
                    e.printStackTrace()
                }
            }
        }
    }

    private fun fetchDistributorLosses(id: String) {
        _uiState.update { oldState ->
            oldState.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            fetchLossesUseCase(
                FetchDistributorLossesRequest(
                    distributorId = id
                )
            ).collect { result ->
                result.onSuccess { data ->
                    launch(Dispatchers.Default) {
                        _uiState.update { oldState ->
                            oldState.copy(
                                losses = data,
                                isLoading = true
                            )
                        }
                        if(uiState.value.selectedDateFilter == DateFormat.CUSTOM_RANGE){
                            filterLosses()
                        } else {
                            convertToDatedLosses()
                        }
                    }
                }.onFailure { e ->
                    Log.e(TAG, "fetchDistributorLosses: ${e.message}", e)
                }
            }
        }
    }

    fun handleAction(action: LossesUiAction) {
        when (action) {
            is LossesUiAction.OnAddLoss -> addLoss(action.loss)
            is LossesUiAction.OnDeleteLoss -> deleteLoss(action.loss)
            is LossesUiAction.OnUpdateLoss -> updateLoss(action.loss)
            is LossesUiAction.UpdateSelectedDateFilter -> updateSelectedFilter(action.filter)
            is LossesUiAction.SetCustomRangeFilter -> setCustomRangeFilter(action.range)
            else -> Unit
        }
    }

    private fun setCustomRangeFilter(range: Pair<Date?, Date?>) {
        viewModelScope.launch {
            if(range.first == null && range.second == null) return@launch
            _uiState.update { it.copy(isLoading = true, selectedDateFilter = DateFormat.CUSTOM_RANGE, selectedDateRange = range) }
            filterLosses()
        }
    }

    private fun filterLosses() {
        viewModelScope.launch (Dispatchers.Default){
            val range = _uiState.value.selectedDateRange
            val filteredLosses = _uiState.value.losses.filter {
                val afterFlag = range.first?.let { date -> it.date.isAfter(date)} ?: true
                val beforeFlag = range.second?.let { date -> it.date.isBefore(date) } ?: true
                beforeFlag && afterFlag
            }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    filteredLosses = filteredLosses
                )
            }
        }
    }

    private fun updateSelectedFilter(filter: DateFormat) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, selectedDateFilter = filter) }
            convertToDatedLosses()
        }
    }

    private fun convertToDatedLosses() {
        viewModelScope.launch(Dispatchers.Default) {
            convertToDatedLossesUseCase(
                losses = uiState.value.losses,
                format = uiState.value.selectedDateFilter
            ).let { datedLosses ->
                _uiState.update { oldState ->
                    oldState.copy(
                        datedLosses = datedLosses,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun updateLoss(loss: DistributorLoss) {
        viewModelScope.launch(Dispatchers.IO) {
            val newLogs = loss.logs.toMutableList()
            val distributor = _uiState.value.currentUser
            newLogs.add(
                ChangeLog(
                    date = Date(),
                    employeeId = distributor.id,
                    employeeName = distributor.fullName,
                    type = LogType.UPDATE
                )
            )
            updateLossUseCase(
                UpdateDistributorLossRequest(
                    loss = loss.copy(logs = newLogs)
                )
            ).onSuccess {
                Log.d(TAG, "updateLoss: Success")
            }.onFailure { e ->
                Log.e(TAG, "updateLoss: ${e.message}", e)
            }
        }
    }

    private fun deleteLoss(loss: DistributorLoss) {
        viewModelScope.launch(Dispatchers.IO) {
            val newLogs = loss.logs.toMutableList()
            val distributor = _uiState.value.currentUser
            newLogs.add(
                ChangeLog(
                    date = Date(),
                    employeeId = distributor.id,
                    employeeName = distributor.fullName,
                    type = LogType.DELETE
                )
            )
            updateLossUseCase(
                UpdateDistributorLossRequest(
                    loss = loss.copy(logs = newLogs, deleted = true)
                )
            ).onSuccess {
                Log.d(TAG, "deleteLoss: Success")
            }.onFailure { e ->
                Log.e(TAG, "deleteLoss: ${e.message}", e)
            }
        }
    }

    private fun addLoss(loss: DistributorLoss) {
        viewModelScope.launch(Dispatchers.IO) {
            val distributor = _uiState.value.currentUser
            val newLogs = listOf(
                ChangeLog(
                    date = Date(),
                    employeeId = distributor.id,
                    employeeName = distributor.fullName,
                    type = LogType.CREATE
                )
            )
            addLossUseCase(
                AddDistributorLossRequest(
                    loss = loss.copy(
                        logs = newLogs,
                        date = Date(),
                        userId = distributor.id,
                        userName = distributor.fullName,
                    )
                )
            ).onSuccess {
                Log.d(TAG, "addLoss: Success")
            }.onFailure { e ->
                Log.e(TAG, "addLoss: ${e.message}", e)
            }
        }
    }
}
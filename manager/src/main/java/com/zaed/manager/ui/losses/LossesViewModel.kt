package com.zaed.manager.ui.losses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.common.data.model.loss.ManagerLoss
import com.zaed.common.data.model.loss.ManagerLossType
import com.zaed.common.data.model.loss.request.AddManagerLossRequest
import com.zaed.common.data.model.loss.request.DeleteManagerLossRequest
import com.zaed.common.data.model.loss.request.UpdateManagerLossRequest
import com.zaed.common.domain.loss.AddManagerLossUseCase
import com.zaed.common.domain.loss.ConvertLossesToDatedLossesUseCase
import com.zaed.common.domain.loss.DeleteManagerLossUseCase
import com.zaed.common.domain.loss.FetchManagerLossesUseCase
import com.zaed.common.domain.loss.UpdateManagerLossUseCase
import com.zaed.common.ui.util.DateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class LossesViewModel(
    private val convertLossesToDatedLossesUseCase: ConvertLossesToDatedLossesUseCase,
    private val fetchLossesUseCase: FetchManagerLossesUseCase,
    private val addLossUseCase: AddManagerLossUseCase,
    private val updateLossUseCase: UpdateManagerLossUseCase,
    private val deleteLossUseCase: DeleteManagerLossUseCase
) : ViewModel() {
    private val TAG: String = "LossesViewModel"
    private val _uiState = MutableStateFlow(LossesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchLosses()
    }

    private fun fetchLosses() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchLossesUseCase().collect { result ->
                result.onSuccess { data ->
                    val groupedLosses = data.groupBy { it.type == ManagerLossType.PERSONAL_EXPENSE }
                    _uiState.update { oldState ->
                        oldState.copy(
                            personalExpenses = groupedLosses[true] ?: emptyList(),
                            losses = groupedLosses[false] ?: emptyList()
                        )
                    }
                    if(uiState.value.selectedLossesFilter == DateFormat.CUSTOM_RANGE){
                        filterLosses()
                    } else {
                        convertLossesToDated()
                    }
                    if(uiState.value.selectedPersonalExpensesFilter == DateFormat.CUSTOM_RANGE){
                        filterPersonalExpenses()
                    } else {
                        convertPersonalExpensesToDated()
                    }
                }.onFailure { e ->
                    Log.e(TAG, "fetchLosses: ${e.message}", e)
                }
            }
        }
    }

    fun handleAction(action: LossesUiAction) {
        when (action) {
            is LossesUiAction.DeleteLoss -> deleteLoss(action.loss)
            is LossesUiAction.SaveLoss -> onSave(action.loss)
            is LossesUiAction.UpdateLossesDateFilter -> updateLossesDateFilter(action.filter)
            is LossesUiAction.UpdatePersonalExpensesDateFilter -> updatePersonalExpensesDateFilter(
                action.filter
            )
            is LossesUiAction.SetCustomLossesRange -> setLossesRange(action.range)
            is LossesUiAction.SetCustomPersonalExpensesRange -> setPersonalExpensesRange(action.range)
            else -> {}
        }
    }

    private fun setPersonalExpensesRange(range: Pair<Date?, Date?>) {
        viewModelScope.launch {
            if(range.first == null && range.second == null) return@launch
            _uiState.update {
                it.copy(
                    isLoading = true,
                    personalExpensesDateRange = range,
                    selectedPersonalExpensesFilter = DateFormat.CUSTOM_RANGE
                )
            }
            filterPersonalExpenses()
        }
    }

    private fun filterPersonalExpenses() {
        viewModelScope.launch (Dispatchers.Default){
            val filteredPersonalExpenses = _uiState.value.personalExpenses.filter { loss ->
                val fromFlag = _uiState.value.personalExpensesDateRange.first?.let { it <= loss.date } ?: true
                val toFlag = _uiState.value.personalExpensesDateRange.second?.let { it >= loss.date } ?: true
                fromFlag && toFlag
            }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    filteredPersonalExpenses = filteredPersonalExpenses
                )
            }
        }
    }

    private fun setLossesRange(range: Pair<Date?, Date?>) {
        viewModelScope.launch {
            if(range.first == null && range.second == null) return@launch
            _uiState.update {
                it.copy(
                    isLoading = true,
                    lossesDateRange = range,
                    selectedPersonalExpensesFilter = DateFormat.CUSTOM_RANGE
                )
            }
            filterLosses()
        }
    }

    private fun filterLosses() {
        viewModelScope.launch (Dispatchers.Default){
            val filteredLosses = _uiState.value.losses.filter { loss ->
                val fromFlag = _uiState.value.lossesDateRange.first?.let { it <= loss.date } ?: true
                val toFlag = _uiState.value.lossesDateRange.second?.let { it >= loss.date } ?: true
                fromFlag && toFlag
            }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    filteredLosses = filteredLosses
                )
            }
        }
    }

    private fun onSave(loss: ManagerLoss) {
        if (loss.id.isBlank()) {
            addLoss(loss)
        } else {
            updateLoss(loss)
        }
    }

    private fun updateLoss(loss: ManagerLoss) {
        viewModelScope.launch (Dispatchers.IO){
            updateLossUseCase(
                UpdateManagerLossRequest(
                    loss = loss
                )
            ).onSuccess {
                Log.d(TAG, "updateLoss: success")
            }.onFailure {
                Log.e(TAG, "updateLoss: ${it.message}",it )
            }
        }
    }

    private fun addLoss(loss: ManagerLoss) {
        viewModelScope.launch (Dispatchers.IO){
            addLossUseCase(
                AddManagerLossRequest(
                    loss = loss.copy(
                        date = Date()
                    )
                )
            ).onSuccess {
                Log.d(TAG, "addLoss: success")
            }.onFailure {
                Log.e(TAG, "addLoss: ${it.message}", it)
            }
        }
    }

    private fun deleteLoss(loss: ManagerLoss) {
        viewModelScope.launch (Dispatchers.IO){
            deleteLossUseCase(
                DeleteManagerLossRequest(
                    loss = loss
                )
            ).onSuccess {
                Log.d(TAG, "deleteLoss: success")
            }.onFailure {
                Log.e(TAG, "deleteLoss: ${it.message}", it)
            }
        }
    }

    private fun updatePersonalExpensesDateFilter(filter: DateFormat) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(
                    isLoading = true,
                    selectedPersonalExpensesFilter = filter
                )
            }
            convertPersonalExpensesToDated()
        }
    }

    private fun updateLossesDateFilter(filter: DateFormat) {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(
                    isLoading = true,
                    selectedLossesFilter = filter
                )
            }
            convertLossesToDated()
        }
    }

    private fun convertPersonalExpensesToDated() {
        viewModelScope.launch(Dispatchers.Default) {
            convertLossesToDatedLossesUseCase(
                _uiState.value.personalExpenses,
                _uiState.value.selectedPersonalExpensesFilter
            ).let { datedPersonalExpenses ->
                _uiState.update { oldState ->
                    oldState.copy(
                        isLoading = false,
                        datedPersonalExpenses = datedPersonalExpenses
                    )
                }
            }
        }
    }

    private fun convertLossesToDated() {
        viewModelScope.launch(Dispatchers.Default) {
            convertLossesToDatedLossesUseCase(
                _uiState.value.losses,
                _uiState.value.selectedLossesFilter
            ).let { datedLosses ->
                _uiState.update { oldState ->
                    oldState.copy(
                        isLoading = false,
                        datedLosses = datedLosses
                    )
                }
            }
        }
    }
}
package com.zaed.cashier.ui.loss

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaed.cashier.domain.loss.CreateNewLossRequest
import com.zaed.cashier.domain.loss.CreateNewLossUseCase
import com.zaed.cashier.domain.loss.GetAllLossesRequest
import com.zaed.cashier.domain.loss.GetAllLossesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LossViewModel(
    private val createNewLossUseCase: CreateNewLossUseCase,
    private val getAllLossesUseCase: GetAllLossesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(LossUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getAllLosses()
    }

    private fun getAllLosses() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllLossesUseCase(GetAllLossesRequest()).collect { result ->
                result.onSuccess { data ->
                    _uiState.update { it.copy(losses = data) }
                }.onFailure { error ->
                        _uiState.update { it.copy(errorMessage = error as? LossError) }
                }
            }
        }
    }

    fun handleAction(action: LossUiAction) {
        when (action) {
            is LossUiAction.OnCreateNewLoss -> submit(
                value = action.value,
                reason = action.reason
            )
            LossUiAction.ResetError -> resetError()
            LossUiAction.ResetSuccess -> resetSuccessState()
            else -> {}
        }
    }


    private fun submit(
        value: String,
        reason: String
    ) {
        if (!validateInput(
            value = value,
            reason = reason
        )) return
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            createNewLossUseCase(
                CreateNewLossRequest(
                    value = value.toDouble(),
                    reason = reason
                )
            ).onSuccess {
                _uiState.update { it.copy(isLoading = false, successMessage = "Loss created successfully") }
                getAllLosses()
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error as? LossError) }
            }
        }
    }

    private fun validateInput(
        value: String,
        reason: String
    ): Boolean {
        when {
            !value.isDigitsOnly() -> {
                _uiState.update { it.copy(fieldError = LossFieldsError.LOSS_VALUE_IS_INVALID) }
                return false
            }

            (value.toDoubleOrNull() ?: 0.0) <= 0.0 -> {
                _uiState.update { it.copy(fieldError = LossFieldsError.LOSS_VALUE_IS_EMPTY) }
                return false
            }

            reason.isEmpty() -> {
                _uiState.update { it.copy(fieldError = LossFieldsError.LOSS_REASON_IS_EMPTY) }
                return false
            }

            else -> {
                _uiState.update { it.copy(fieldError = LossFieldsError.NONE) }
                return true
            }
        }
    }

    private fun resetSuccessState() {
        _uiState.update { it.copy(successMessage = null) }
    }

    private fun resetError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }
}